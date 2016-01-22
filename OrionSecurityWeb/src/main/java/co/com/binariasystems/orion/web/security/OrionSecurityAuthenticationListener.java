package co.com.binariasystems.orion.web.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;

import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.orion.business.bean.services.SecurityBean;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;

public class OrionSecurityAuthenticationListener implements AuthenticationListener{
	private SecurityBean securityBean;
	
	public OrionSecurityAuthenticationListener(){
		securityBean = IOCHelper.getBean(SecurityBean.class);
	}

	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
	}

	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		AccessTokenDTO accessToken= (AccessTokenDTO)getAvailablePrincipal(principals);
		securityBean.invalidateUserSession(new AuthenticationDTO(accessToken.getUser().getLoginAlias(), accessToken.getApplication().getApplicationCode().name()));
	}
	
	protected Object getAvailablePrincipal(PrincipalCollection principals) {
        return principals.getPrimaryPrincipal();
    }

}
