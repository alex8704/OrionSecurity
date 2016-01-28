package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.orion.web.Orion;

public interface OrionWebConstants {
	final String MESSAGES_PACKAGE = "co.com.binariasystems.orion.web.resources.messages";
	final String ADMIN_VIEW_MESSAGES = MESSAGES_PACKAGE+".admin_view_strings";
	final String MAIN_MESSAGES_FILE = "main_view_messages";
	final String EXCEPTION_MSG_HEADER_NAME = "orion-service-errormsg";
	final String EXCEPTION_TYPE_HEADER_NAME = "orion-service-errortype";
	final String OPEN_POPUP_VIEW_FUNCTION = Orion.class.getName()+".dispatchPopupView";
}
