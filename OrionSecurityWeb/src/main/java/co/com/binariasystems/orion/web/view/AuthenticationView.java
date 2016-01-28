package co.com.binariasystems.orion.web.view;

import co.com.binariasystems.fmw.vweb.constants.UIConstants;
import co.com.binariasystems.fmw.vweb.mvp.annotation.AuthenticationForm;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View.Root;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.NullValidator;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.RegExpValidator;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.StringLengthValidator;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.PasswordFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.web.controller.AuthenticationViewController;
import co.com.binariasystems.orion.web.resources.resources;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

@AuthenticationForm
@Root
@View(url="/", messages=AuthenticationView.MESSAGES_BUNDLE, controller=AuthenticationViewController.class, viewStringsByConventions= true)
public class AuthenticationView extends AbstractView implements UIConstants, OrionWebConstants {
	public static final String		MESSAGES_BUNDLE = OrionWebConstants.MESSAGES_PACKAGE + "." + MAIN_MESSAGES_FILE;
	private VerticalLayout 				mainContainer;
	@NoConventionString
	private FormPanel					loginForm;
	private Image						applicationLogo;
	private LabelBuilder				welcomeLbl;
	@PropertyId("username")
	@NullValidator
	@StringLengthValidator(min=6, max=30)
	private TextFieldBuilder			usernameTxt;
	@PropertyId("password")
	@NullValidator
	@RegExpValidator(expression="[a-zA-Z_0-9]{6,30}")
	private PasswordFieldBuilder		passwordTxt;
	@PropertyId("rememberMe")
	private CheckBox					rememberMeChk;
	private ButtonBuilder				authenticateBtn;
	private BeanFieldGroup<AuthenticationDTO> fieldGroup;
	private Dimension loginPanelWidth = Dimension.pixels(300);
	private AuthenticationDTO			authenticationDTO;
	
	
	@ViewBuild
	public Component build(){
		//Instanciado de los componentes de la vista
		mainContainer = new VerticalLayout();
		loginForm = new FormPanel(2);
		applicationLogo = new Image(null, new ClassResource(resources.class, resources.getImageFilePath("yes.png")));
		welcomeLbl = new LabelBuilder();
		usernameTxt = new TextFieldBuilder();
		passwordTxt = new PasswordFieldBuilder();
		rememberMeChk = new CheckBox();
		authenticateBtn = new ButtonBuilder();
		authenticationDTO = new AuthenticationDTO();
		fieldGroup = new BeanFieldGroup<AuthenticationDTO>(AuthenticationDTO.class);
		fieldGroup.setItemDataSource(authenticationDTO);
		
		//Customizacion y ubicacion de los componentes de la vista
		fieldGroup.setBuffered(false);
		fieldGroup.bindMemberFields(this);

		mainContainer.setSizeFull();
		mainContainer.addComponent(loginForm);
		loginForm.addEmptyRow()
		.add(applicationLogo)
		.add(welcomeLbl)
		.add(usernameTxt, 2)
		.add(passwordTxt,2)
		.add(rememberMeChk, 2)
		.addCenteredOnNewRow(authenticateBtn)
		.addEmptyRow();
		
		mainContainer.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
		loginForm.setWidth(loginPanelWidth.value, loginPanelWidth.unit);
		loginForm.setColumnExpandRatio(1, 1.0f);
		welcomeLbl.withStyleNames(CENTER_ALIGN_STYLE);
		welcomeLbl.withFullWidth();
		usernameTxt.withFullWidth().withoutUpperTransform();
		passwordTxt.withFullWidth();
		return mainContainer;
	}
}
