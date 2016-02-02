package co.com.binariasystems.orion.web.utils;

import java.util.Map;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.util.StringUtils;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.MVPUtils;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.ViewDispatcher;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.ViewProvider;

import com.vaadin.server.Page;

public final class OrionWebUtils {
	
	public static void modalPopup(String targetURL){
		modalPopup(targetURL, null);
	}
	
	public static void modalPopup(String targetURL, Map<String, String> parameters){
		StringBuilder invocationBuilder = new StringBuilder();
		
		String prefixedURL = targetURL.startsWith("/") ? ViewDispatcher.POPUP_REQUEST_PREFIX + targetURL : ViewDispatcher.POPUP_REQUEST_PREFIX +"/"+ targetURL;
		invocationBuilder.append("javascript:")
		.append(OrionWebConstants.OPEN_POPUP_VIEW_FUNCTION)
		.append(FMWConstants.LPARENTHESIS)
		.append(StringUtils.singleQuote(MVPUtils.encodeURI(prefixedURL+parseMapToURIInfo(parameters, targetURL.contains("?") ? '&' : '?'))))
		.append(FMWConstants.RPARENTHESIS);
		Page.getCurrent().getJavaScript().execute(invocationBuilder.toString());
	}
	
	public static String parseMapToURIInfo(Map<String, String> parameters, char initialChar){
		StringBuilder builder = new StringBuilder();
		if(parameters == null)
			builder.append(initialChar).append("popup=true");
		if(parameters != null){
			if(!parameters.containsKey("popup"))
				builder.append(initialChar).append("popup=true");
			for(String paramName : parameters.keySet())
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(parameters.get(paramName)))
					builder.append(builder.length() == 0 ? initialChar : "&")
					.append(paramName).append("=").append(parameters.get(paramName));
		}
		return builder.toString();
	}
	
	public static String getViewURL(Class<?> viewClass){
		return IOCHelper.getBean(ViewProvider.class).getViewUrlByClass(viewClass);
	}
}
