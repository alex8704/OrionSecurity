package co.com.binariasystems.orion.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import co.com.binariasystems.fmw.security.auditory.ShiroBasedAuditoryDataProvider;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;

public class OrionAuditoryDataProvider extends ShiroBasedAuditoryDataProvider<AccessTokenDTO> {

	@Override
	public AccessTokenDTO getCurrenAuditoryUser(HttpSession httpSession) {
		return null;
	}

	@Override
	public Object gettCurrentAuditoryUserForEntityCRUD(HttpSession httpSession) {
		return null;
	}

	@Override
	public Object gettCurrentAuditoryUserForEntityCRUD(HttpServletRequest httpRequest) {
		return getCurrenAuditoryUser(httpRequest).getUser().getLoginAlias();
	}

}
