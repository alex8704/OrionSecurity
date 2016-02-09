package co.com.binariasystems.orion.business.bean.services;

import java.util.List;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;
import co.com.binariasystems.orion.business.SecurityServicesException;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;


public interface SecurityBean {
	
	public UserDTO findUserByLoginAlias(String loginAlias);
	
	public AccessTokenDTO saveAuthentication(AuthenticationDTO authentication) throws SecurityServicesException;
	
	public void invalidateUserSession(AccessTokenDTO accessTokenDTO);
	
	public SN2Boolean validateAccessTokenValidity(AccessTokenDTO accessTokenDTO);
	
	public SN2Boolean registerFailedAuthententicationAttempt(AuthenticationDTO authentication);
	
	public List<RoleDTO> findUserRoles(AccessTokenDTO accessTokenDTO);
	
	public List<ResourceDTO> findRoleResources(RoleDTO role);
	
	public List<ResourceDTO> findUserResources(AccessTokenDTO accessTokenDTO);
	
}
