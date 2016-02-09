package co.com.binariasystems.orion.web.rest.services;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.orion.business.OrionException;
import co.com.binariasystems.orion.business.SecurityServicesException;
import co.com.binariasystems.orion.business.bean.services.SecurityBean;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserCredentialsDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

@RequestScoped
@Path("security")
public class SecurityServices {
	private SecurityBean bean;
	
	public SecurityServices(){
		bean = IOCHelper.getBean(SecurityBean.class);
	}
	
	@Path("userbyloginalias")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserDTO findUserByLoginAlias(@QueryParam("loginalias") String loginalias) throws OrionException {
		return bean.findUserByLoginAlias(loginalias);
	}
	
	@Path("usercredentials")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserCredentialsDTO findUserCredentials(@QueryParam("loginalias") String loginalias) throws OrionException {
		UserDTO user = bean.findUserByLoginAlias(loginalias);
		if(user != null){
			return user.getCredentials();
		}
		throw new OrionException("Que dijiste coroné?");
	}
	
	@Path("saveauthentication")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public AccessTokenDTO saveAuthentication(AuthenticationDTO authentication) throws SecurityServicesException{
		return bean.saveAuthentication(authentication);
		
	}
	
	@Path("invalidatesession")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public void invalidateUserSession(AccessTokenDTO accessTokenDTO){
		bean.invalidateUserSession(accessTokenDTO);
	}
	
	@Path("accesstokenvalidity")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public SN2Boolean validateAccessTokenValidity(AccessTokenDTO accessTokenDTO){
		return bean.validateAccessTokenValidity(accessTokenDTO);
	}
	
	@Path("userroles")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoleDTO> findUserRoles(AccessTokenDTO accessTokenDTO){
		return bean.findUserRoles(accessTokenDTO);
	}
	
	@Path("roleresources")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResourceDTO> findRoleResources(RoleDTO role){
		return bean.findRoleResources(role);
	}
	
	@Path("userresources")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResourceDTO> findUserResources(AccessTokenDTO accessTokenDTO){
		return bean.findUserResources(accessTokenDTO);
	}
}
