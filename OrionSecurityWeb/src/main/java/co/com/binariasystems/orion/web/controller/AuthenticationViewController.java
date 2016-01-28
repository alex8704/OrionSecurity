package co.com.binariasystems.orion.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import co.com.binariasystems.fmw.security.FMWSecurityException;
import co.com.binariasystems.fmw.security.mgt.SecurityManager;
import co.com.binariasystems.fmw.security.model.AuthenticationRequest;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.MVPUtils;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.FormValidationException;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.PasswordFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.UI;

@ViewController
public class AuthenticationViewController extends AbstractViewController{
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationViewController.class);

	@ViewField
	private TextFieldBuilder usernameTxt;
	@ViewField
	private PasswordFieldBuilder passwordTxt;
	@ViewField
	private CheckBox rememberMeChk;
	@ViewField
	private ButtonBuilder authenticateBtn;
	@ViewField
	private BeanFieldGroup<AuthenticationDTO> fieldGroup;
	@ViewField
	private AuthenticationDTO authenticationDTO;
	@ViewField
	private FormPanel loginForm;
	@Autowired
	private SecurityManager securityManager;
	private AuthentiationViewClickListener clickListener;

	@Init
	public void init() {
		clickListener = new AuthentiationViewClickListener();
		authenticateBtn.addClickListener(clickListener);
	}

	@OnLoad
	public void onLoadController() {
		loginForm.initFocus();
	}

	@OnUnLoad
	public void onUnloadController() {
		for (Object propertyId : fieldGroup.getBoundPropertyIds())
			fieldGroup.getItemDataSource().getItemProperty(propertyId).setValue(null);
	}

	private VaadinServletRequest getVaadinRequest() {
		return (VaadinServletRequest) VaadinService.getCurrentRequest();
	}

	public void loginButtonClick() throws FormValidationException, FMWSecurityException {
		loginForm.validate();
		WebBrowser browser = Page.getCurrent().getWebBrowser();
		AuthenticationRequest authRequest = new AuthenticationRequest();
		authRequest.setUsername(authenticationDTO.getUsername());
		authRequest.setPassword(authenticationDTO.getPassword());
		authRequest.setRememberMe(authenticationDTO.getRememberMe());
		authRequest.setHttpRequest(getVaadinRequest().getHttpServletRequest());
		authRequest.setHost(browser.getAddress());
		securityManager.authenticate(authRequest);
		MVPUtils.navigateTo(securityManager.getDashBoardViewUrl());
	}

	private class AuthentiationViewClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			try {
				loginButtonClick();
			} catch (FormValidationException | FMWSecurityException ex) {
				MessageDialog.showExceptions(ex, LOGGER);
			}
		}

	}
}
