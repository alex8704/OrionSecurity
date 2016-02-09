package co.com.binariasystems.orion.web.security.authz;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.orion.business.bean.services.SecurityBean;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;

public class AccessTokenAuthorizationFilter extends AuthorizationFilter{
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenAuthorizationFilter.class);
	private String tokenHeaderName;
	private SecurityBean securityBean;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		AccessTokenDTO accessTokenDTO = (AccessTokenDTO)SecurityUtils.getSubject().getPrincipal();
		if(httpRequest.getSession().isNew()){
			httpResponse.setHeader(tokenHeaderName, accessTokenDTO.getTokenString());
			return true;
		}
		
		String accessToken = httpRequest.getHeader(tokenHeaderName);
		
		return accessTokenDTO.getTokenString().equals(accessToken);
		
		/**
		 * Cometado por consideraciones de performance, provisoriamente se hara la validacion
		 * contra las credenciales obtenidas del Subject de Shiro, y si luego de estudiar las implicaciones
		 * se considera mas seguro hacerlo contra la BB.DD se habilitara el codigo comentado
		 */
//		AccessTokenDTO validateAccessToken = new AccessTokenDTO();
//		validateAccessToken.setApplication(accessTokenDTO.getApplication());
//		validateAccessToken.setUser(accessTokenDTO.getUser());
//		validateAccessToken.setTokenString(accessToken);
//		
//		return getSecurityBean().validateAccessTokenValidity(validateAccessToken).booleanValue();
		
	}

	/**
	 * @return the tokenHeaderName
	 */
	public String getTokenHeaderName() {
		return tokenHeaderName;
	}

	/**
	 * @param tokenHeaderName the tokenHeaderName to set
	 */
	public void setTokenHeaderName(String tokenHeaderName) {
		this.tokenHeaderName = tokenHeaderName;
	}
	
//	private SecurityBean getSecurityBean(){
//		if(securityBean == null)
//			securityBean = IOCHelper.getBean(SecurityBean.class);
//		return securityBean;
//	}

}
