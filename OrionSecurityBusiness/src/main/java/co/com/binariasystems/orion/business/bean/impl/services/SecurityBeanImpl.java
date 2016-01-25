package co.com.binariasystems.orion.business.bean.impl.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.security.crypto.CredentialsCrypto;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoId;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoProvider;
import co.com.binariasystems.fmw.security.crypto.MatchingRequest;
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
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserCredentialsDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.model.enumerated.Application;
import co.com.binariasystems.orion.model.enumerated.SN2Boolean;
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

	private String privateSalt = "dzNidDNzdDRwcDUzY3VyM3A0NTV3MHJkc2FsdA==";
	private String hashAlgorithm = "SHA-256";
	private int hashIterations = 50000;
	private boolean storedCredentialsHexEncoded = true;
	private int maxAuthenticationRetries = 3;
	
	public UserDTO findUserByLoginAlias(String loginAlias){
		return ObjectUtils.transferPropertiesRecursive(userDAO.findFirstByLoginAlias(loginAlias), UserDTO.class);
	}
	
	@Transactional
	public AccessTokenDTO saveAuthentication(AuthenticationDTO authentication) throws SecurityServicesException{
		SegtUser user = userDAO.findFirstByLoginAlias(authentication.getUsername());
		validateUserAccount(user);
		validateUserBlocking(user);
		if(!credentialsMatches(authentication.getPassword(), ObjectUtils.transferPropertiesRecursive(user.getCredentials(), UserCredentialsDTO.class))){
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
		
		return ObjectUtils.transferPropertiesRecursive(accessToken, AccessTokenDTO.class);
	}
	
	@Transactional
	public void invalidateUserSession(AccessTokenDTO accessTokenDTO){
		SegtAccessToken accessToken = ObjectUtils.transferPropertiesRecursive(accessTokenDTO, SegtAccessToken.class);
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
		CredentialsCrypto credentialsCrypto = CredentialsCryptoProvider.get(CredentialsCryptoId.SHIRO);
		MatchingRequest matchingRequest = new  MatchingRequest();
		matchingRequest.setAlgorithmName(hashAlgorithm);
		matchingRequest.setHashIterations(hashIterations);
		matchingRequest.setHexEncoded(storedCredentialsHexEncoded);
		matchingRequest.setPrivateSalt(privateSalt);
		matchingRequest.setProvidedPassword(providedCredentials);
		matchingRequest.setStoredPassword(storedCredentials.getPassword());
		matchingRequest.setStoredPasswordSalt(storedCredentials.getPasswordSalt());
		return credentialsCrypto.credentialsMatches(matchingRequest);
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
		SegtUser user = ObjectUtils.transferPropertiesRecursive(accessTokenDTO.getUser(), SegtUser.class);
		SegtApplication application = ObjectUtils.transferPropertiesRecursive(accessTokenDTO.getApplication(), SegtApplication.class);
		return ObjectUtils.transferPropertiesListRecursive(roleDAO.findByAssignedUsersAndApplication(user, application), RoleDTO.class);
	}
	
	public List<ResourceDTO> findRoleResources(RoleDTO role){
		List<SegtResource> resources = resourceDAO.findByAuthorizedRolesAndApplication(new SegtRole(role.getRolId()), new SegtApplication(role.getApplication().getApplicationId()));
		return ObjectUtils.transferPropertiesListRecursive(resources, ResourceDTO.class);
	}
	
	public List<ResourceDTO> findUserResources(AccessTokenDTO accessTokenDTO){
		SegtUser user = ObjectUtils.transferPropertiesRecursive(accessTokenDTO.getUser(), SegtUser.class);
		SegtApplication application = ObjectUtils.transferPropertiesRecursive(accessTokenDTO.getApplication(), SegtApplication.class);
		List<SegtRole> roles = roleDAO.findByAssignedUsersAndApplication(user, application);
		List<SegtResource> resources = roles.isEmpty() ? new LinkedList<SegtResource>() :resourceDAO.findByAuthorizedRolesListAndApplication(roles, application);
		return ObjectUtils.transferPropertiesListRecursive(resources, ResourceDTO.class);
	}
}