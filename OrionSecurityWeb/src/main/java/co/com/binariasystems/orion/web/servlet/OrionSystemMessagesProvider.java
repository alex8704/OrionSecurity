package co.com.binariasystems.orion.web.servlet;

import org.apache.commons.lang3.StringUtils;

import co.com.binariasystems.fmw.util.messagebundle.MessageBundleManager;
import co.com.binariasystems.orion.web.resources.resources;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;

public class OrionSystemMessagesProvider implements SystemMessagesProvider, OrionWebConstants {

private MessageBundleManager messages;
	
	
	public OrionSystemMessagesProvider(){
		messages = MessageBundleManager.forPath(resources.getMessageFilePath(MAIN_MESSAGES_FILE), false);
	}
	
	@Override
	public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
		CustomizedSystemMessages customizedMessages = new CustomizedSystemMessages();
		customizedMessages.setSessionExpiredURL(messages.getString("Orion.sessionExpiredURL", systemMessagesInfo.getLocale()));
		customizedMessages.setSessionExpiredNotificationEnabled(Boolean.valueOf(StringUtils.defaultIfBlank(messages.getString("Orion.sessionExpiredNotificationEnabled", systemMessagesInfo.getLocale()), "true")));
		customizedMessages.setSessionExpiredCaption(messages.getString("Orion.sessionExpiredCaption", systemMessagesInfo.getLocale()));
		customizedMessages.setSessionExpiredMessage(messages.getString("Orion.sessionExpiredMessage", systemMessagesInfo.getLocale()));
		
		customizedMessages.setCommunicationErrorURL(messages.getString("Orion.communicationErrorURL", systemMessagesInfo.getLocale()));
		customizedMessages.setCommunicationErrorNotificationEnabled(Boolean.valueOf(StringUtils.defaultIfBlank(messages.getString("Orion.communicationErrorNotificationEnabled", systemMessagesInfo.getLocale()), "true")));
		customizedMessages.setCommunicationErrorCaption(messages.getString("Orion.communicationErrorCaption", systemMessagesInfo.getLocale()));
		customizedMessages.setCommunicationErrorMessage(messages.getString("Orion.communicationErrorMessage", systemMessagesInfo.getLocale()));
		
		customizedMessages.setAuthenticationErrorURL(messages.getString("Orion.authenticationErrorURL", systemMessagesInfo.getLocale()));
		customizedMessages.setAuthenticationErrorNotificationEnabled(Boolean.valueOf(StringUtils.defaultIfBlank(messages.getString("Orion.authenticationErrorNotificationEnabled", systemMessagesInfo.getLocale()), "true")));
		customizedMessages.setAuthenticationErrorCaption(messages.getString("Orion.authenticationErrorCaption", systemMessagesInfo.getLocale()));
		customizedMessages.setAuthenticationErrorMessage(messages.getString("Orion.authenticationErrorMessage", systemMessagesInfo.getLocale()));
		
		customizedMessages.setInternalErrorURL(messages.getString("Orion.internalErrorURL", systemMessagesInfo.getLocale()));
		customizedMessages.setInternalErrorNotificationEnabled(Boolean.valueOf(StringUtils.defaultIfBlank(messages.getString("Orion.internalErrorNotificationEnabled", systemMessagesInfo.getLocale()), "true")));
		customizedMessages.setInternalErrorCaption(messages.getString("Orion.internalErrorCaption", systemMessagesInfo.getLocale()));
		customizedMessages.setInternalErrorMessage(messages.getString("Orion.internalErrorMessage", systemMessagesInfo.getLocale()));
		
		customizedMessages.setCookiesDisabledURL(messages.getString("Orion.cookiesDisabledURL", systemMessagesInfo.getLocale()));
		customizedMessages.setCookiesDisabledNotificationEnabled(Boolean.valueOf(StringUtils.defaultIfBlank(messages.getString("Orion.cookiesDisabledNotificationEnabled", systemMessagesInfo.getLocale()), "true")));
		customizedMessages.setCookiesDisabledCaption(messages.getString("Orion.cookiesDisabledCaption", systemMessagesInfo.getLocale()));
		customizedMessages.setCookiesDisabledMessage(messages.getString("Orion.cookiesDisabledMessage", systemMessagesInfo.getLocale()));
		
		return customizedMessages;
	}

}
