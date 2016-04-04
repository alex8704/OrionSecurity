package co.com.binariasystems.orion.web.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.LocaleUtils;

import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.security.mgt.SecurityManager;
import co.com.binariasystems.fmw.security.model.AuthorizationRequest;
import co.com.binariasystems.fmw.vweb.constants.VWebCommonConstants;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;

public class OrionSessionListener implements SessionInitListener, SessionDestroyListener {

	@Override
	public void sessionDestroy(SessionDestroyEvent event) {
		VaadinRequest request = VaadinService.getCurrentRequest();
		HttpServletRequest httpRequest = request != null ? ((VaadinServletRequest)request).getHttpServletRequest() : null;
		getSecurityManager().logout(new AuthorizationRequest(null, httpRequest, httpRequest != null ? httpRequest.getSession() : null));
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		//event.getSession().setErrorHandler(new UIErrorHandler());
		Cookie langCookie = null;
		if (event.getRequest() != null) {
			for (Cookie cookie : event.getRequest().getCookies())
				if (cookie.getName().equals(VWebCommonConstants.USER_LANGUAGE_APPCOOKIE)) {
					langCookie = cookie;
					break;
				}
		}

		if (langCookie == null)
			langCookie = createLanguageCookie(event.getRequest());
		event.getSession().setLocale(LocaleUtils.toLocale(langCookie.getValue()));
		VaadinService.getCurrentResponse().addCookie(langCookie);
	}
	
	private Cookie createLanguageCookie(VaadinRequest request){
		Cookie langCookie = new Cookie(VWebCommonConstants.USER_LANGUAGE_APPCOOKIE, request.getLocale().toString());
		langCookie.setPath(request.getContextPath());
		return langCookie;
	}
	
	private SecurityManager getSecurityManager(){
		return IOCHelper.getBean(SecurityManager.class);
	}

}
