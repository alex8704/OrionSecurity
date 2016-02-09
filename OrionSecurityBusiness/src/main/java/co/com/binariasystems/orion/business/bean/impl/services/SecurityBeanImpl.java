package co.com.binariasystems.orion.business.bean.impl.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.commonsmodel.enumerated.Application;
import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;
import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.orion.business.SecurityServicesException;
import co.com.binariasystems.orion.business.bean.services.SecurityBean;
import co.com.binariasystems.orion.business.dao.AccessTokenDAO;
import co.com.binariasystems.orion.business.dao.ApplicationDAO;
import co.com.binariasystems.orion.business.dao.ResourceDAO;
import co.com.binariasystems.orion.business.dao.RoleDAO;
import co.com.binariasystems.orion.business.dao.UserDAO;
import co.com.binariasystems.orion.business.entity.SegtAccessToken;
import co.com.binariasystems.orion.business.entity.SegtAccessTokenPK;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtResource;
import co.com.binariasystems.orion.business.entity.SegtRole;
import co.com.binariasystems.orion.business.entity.SegtUser;
import co.com.binariasystems.orion.business.helper.AccessTokenHelper;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserCredentialsDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.model.enumerated.SecurityExceptionType;

@Service
@Transactional
public class SecurityBeanImpl implements SecurityBean {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AccessTokenDAO accessTokenDAO;
	@Autowired
	private ApplicationDAO applicationDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private ResourceDAO resourceDAO;
	private int maxAuthenticationRetries = 3;
	
	public UserDTO findUserByLoginAlias(String loginAlias){
		return ObjectUtils.transferProperties(userDAO.findFirstByLoginAlias(loginAlias), UserDTO.class);
	}
	
	@Transactional
	public AccessTokenDTO saveAuthentication(AuthenticationDTO authentication) throws SecurityServicesException{
		SegtUser user = userDAO.findFirstByLoginAlias(authentication.getUsername());
		validateUserAccount(user);
		validateUserBlocking(user);
		if(!credentialsMatches(authentication.getPassword(), ObjectUtils.transferProperties(user.getCredentials(), UserCredentialsDTO.class))){
			if(!registerFailedAuthententicationAttempt(authentication).booleanValue())
				throw new SecurityServicesException(SecurityExceptionType.MAX_AUTHENTICATION_ATTEMPTS, "Maximum authentication attempts exceeded");
			throw new SecurityServicesException(SecurityExceptionType.CREDENTIALS_NOT_MATCH, "Authentication credentials has not correct");
		}
		
		SegtApplication application = applicationDAO.findByApplicationCode(Application.valueOf(authentication.getApplicationCode()));
		SegtAccessToken accessToken = accessTokenDAO.findOne(new SegtAccessTokenPK(user.getUserId(), application.getApplicationId()));
		if(accessToken == null)
			accessToken = new SegtAccessToken();
		accessToken.setId(new SegtAccessTokenPK(user.getUserId(), application.getApplicationId()));
		accessToken.setApplication(application);
		accessToken.setCreationDate(new Date());
		accessToken.setIsActive(SN2Boolean.S);
		accessToken.setExpirationDate(null);
		accessToken.setUser(user);
		accessToken.setTokenString(AccessTokenHelper.generateRandomTokenStr());
		
		user.setLastAccessIP(authentication.getNetAddress());
		user.setLastAccessDate(new Date());
		user.setFailedRetries(0);
		user.setIsBlockedByMaxRetries(SN2Boolean.N);
		user.setBlockingDate(null);
		
		userDAO.save(user);
		accessToken = accessTokenDAO.save(accessToken);
		
		return ObjectUtils.transferProperties(accessToken, AccessTokenDTO.class);
	}
	
	@Transactional
	public void invalidateUserSession(AccessTokenDTO accessTokenDTO){
		SegtAccessToken accessToken = ObjectUtils.transferProperties(accessTokenDTO, SegtAccessToken.class);
		accessToken.setId(new SegtAccessTokenPK(accessToken.getUser().getUserId(), accessToken.getApplication().getApplicationId()));
		accessToken.setExpirationDate(new Date());
		accessToken.setIsActive(SN2Boolean.N);
		
		accessTokenDAO.save(accessToken);
	}
	
	public SN2Boolean validateAccessTokenValidity(AccessTokenDTO accessTokenDTO){
		SegtAccessToken accessToken = null;
		if(accessTokenDTO != null && accessTokenDTO.getUser() != null && accessTokenDTO.getApplication() != null)
			accessToken = accessTokenDAO.findOne(new SegtAccessTokenPK(accessTokenDTO.getUser().getUserId(), accessTokenDTO.getApplication().getApplicationId()));
		boolean booleanValue = (accessToken != null && 
				accessToken.getTokenString().equals(accessTokenDTO.getTokenString()) && 
				accessToken.getIsActive().booleanValue());
		return SN2Boolean.fromBoolean(booleanValue);
	}
	
	@Transactional
	public SN2Boolean registerFailedAuthententicationAttempt(AuthenticationDTO authentication){
		SegtUser user = userDAO.findFirstByLoginAlias(authentication.getUsername());
		Integer currentRetries = user.getFailedRetries() != null ? user.getFailedRetries() : Integer.valueOf(0);
		user.setFailedRetries(currentRetries + 1);
		if(user.getFailedRetries() >= maxAuthenticationRetries){
			user.setBlockingDate(SN2Boolean.S.equals(user.getIsActive()) ? new Date() : user.getBlockingDate());
			user.setIsActive(SN2Boolean.N);
			user.setIsBlockedByMaxRetries(SN2Boolean.S);
		}
		
		return userDAO.save(user).getIsActive();
	}
	
	
	private boolean credentialsMatches(String providedCredentials, UserCredentialsDTO storedCredentials){
		return OrionBusinessUtils.credentialsMatches(providedCredentials, storedCredentials.getPassword(), storedCredentials.getPasswordSalt());
	}
	
	private void validateUserAccount(SegtUser user) throws SecurityServicesException{
		if(user == null)
			throw new SecurityServicesException(SecurityExceptionType.INVALID_ACCOUNT, "Account is not valid");
	}
	
	private void validateUserBlocking(SegtUser user) throws SecurityServicesException{
		if(SN2Boolean.S.equals(user.getIsBlockedByMaxRetries()) || !SN2Boolean.S.equals(user.getIsActive()))
			throw new SecurityServicesException(SecurityExceptionType.ACCOUNT_BLOCKED, "Account is locked");
	}
	
	public List<RoleDTO> findUserRoles(AccessTokenDTO accessTokenDTO){
		SegtUser user = ObjectUtils.transferProperties(accessTokenDTO.getUser(), SegtUser.class);
		SegtApplication application = ObjectUtils.transferProperties(accessTokenDTO.getApplication(), SegtApplication.class);
		return ObjectUtils.transferProperties(roleDAO.findByAssignedUsersAndApplication(user, application), RoleDTO.class);
	}
	
	public List<ResourceDTO> findRoleResources(RoleDTO role){
		List<SegtResource> resources = resourceDAO.findByAuthorizedRolesAndApplication(new SegtRole(role.getRolId()), new SegtApplication(role.getApplication().getApplicationId()));
		return ObjectUtils.transferProperties(resources, ResourceDTO.class);
	}
	
	public List<ResourceDTO> findUserResources(AccessTokenDTO accessTokenDTO){
		SegtUser user = ObjectUtils.transferProperties(accessTokenDTO.getUser(), SegtUser.class);
		SegtApplication application = ObjectUtils.transferProperties(accessTokenDTO.getApplication(), SegtApplication.class);
		List<SegtRole> roles = roleDAO.findByAssignedUsersAndApplication(user, application);
		List<SegtResource> resources = roles.isEmpty() ? new LinkedList<SegtResource>() :resourceDAO.findByAuthorizedRolesListAndApplication(roles, application);
		return ObjectUtils.transferProperties(resources, ResourceDTO.class);
	}
}
