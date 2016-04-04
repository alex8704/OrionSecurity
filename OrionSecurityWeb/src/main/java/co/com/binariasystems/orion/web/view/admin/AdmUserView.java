package co.com.binariasystems.orion.web.view.admin;

import java.util.HashMap;
import java.util.Map;

import co.com.binariasystems.fmw.vweb.constants.UIConstants;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.EmailValidator;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.StringLengthValidator;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.Pager;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.DateFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.PasswordFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.fmw.vweb.util.GridUtils;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmUserViewController;
import co.com.binariasystems.orion.web.utils.SN2BooleanConverter;
import co.com.binariasystems.orion.web.utils.SN2BooleanPropertyGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.ImageRenderer;

@View(url="/user", viewStringsByConventions=true, controller=AdmUserViewController.class)
public class AdmUserView extends AbstractView {
	private FormPanel 					form;
	@StringLengthValidator(min=6, max=32)
	@PropertyId("credentials.password")
	private PasswordFieldBuilder		credentialsTxt;
	@StringLengthValidator(min=6, max=30)
	@PropertyId("loginAlias")
	private TextFieldBuilder			loginAliasTxt;
	@StringLengthValidator(min=6, max=15)
	@PropertyId("identificationNumber")
	private TextFieldBuilder			identificationNumberTxt;
	@StringLengthValidator(min=6, max=128)
	@PropertyId("fullName")
	private TextFieldBuilder			fullNameTxt;
	@EmailValidator
	@PropertyId("emailAddress")
	private TextFieldBuilder			emailAddressTxt;
	@PropertyId("lastAccessIP")
	private TextFieldBuilder			lastAccessIPTxt;
	@PropertyId("isoLanguajeCode")
	private TextFieldBuilder			isoLanguajeCodeTxt;
	@PropertyId("failedRetries")
	private TextFieldBuilder			failedRetriesTxt;//ReadOnly
	@PropertyId("lastAccessDate")
	private DateFieldBuilder			lastAccessDateDf;
	@PropertyId("blockingDate")
	private DateFieldBuilder			blockingDateDf;
	@PropertyId("isActive")
	private CheckBox					isActiveChk;
	@PropertyId("isBlockedByMaxRetries")
	private CheckBox					isBlockedByMaxRetriesChk;
	
	private	SortableBeanContainer<UserDTO>	userGridItems;
	private GeneratedPropertyContainer 	userGridDS;
	private Grid						userGrid;
	private Pager<UserDTO, UserDTO>		pager;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder				saveBtn,
										editBtn,
										searchBtn;
	
	@NoConventionString
	private AuthorizeUserRolesWindow	rolesWindow;
	
	private Map<String, String>				notificationMsgMapping;
	private Map<Button, MessageDialog>		confirmMsgDialogMapping;
	private BeanFieldGroup<UserDTO>		fieldGroup;
	private UserDTO currentUser;
	private AdmUserViewEventListener	eventListener;
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		loginAliasTxt = new TextFieldBuilder();
		credentialsTxt = new PasswordFieldBuilder();
		identificationNumberTxt = new TextFieldBuilder();
		fullNameTxt = new TextFieldBuilder();
		emailAddressTxt = new TextFieldBuilder();
		isoLanguajeCodeTxt = new TextFieldBuilder();
		lastAccessIPTxt = new TextFieldBuilder();
		lastAccessDateDf = new DateFieldBuilder();
		blockingDateDf = new DateFieldBuilder();
		failedRetriesTxt = new TextFieldBuilder();
		isActiveChk = new CheckBox();
		isBlockedByMaxRetriesChk = new CheckBox();
		saveBtn = new ButtonBuilder();
		editBtn = new ButtonBuilder();
		searchBtn = new ButtonBuilder();
		userGrid = new Grid();
		pager = new Pager<UserDTO, UserDTO>();
		rolesWindow = new AuthorizeUserRolesWindow();
		addDataBinding();
		
		form.add(loginAliasTxt, Dimension.percent(100))
		.add(credentialsTxt, Dimension.percent(100))
		.add(identificationNumberTxt, Dimension.percent(100))
		.add(fullNameTxt, Dimension.percent(100))
		.add(emailAddressTxt, Dimension.percent(100))
		.add(isoLanguajeCodeTxt, Dimension.percent(100))
		.add(lastAccessIPTxt, Dimension.percent(100))
		.add(lastAccessDateDf, Dimension.percent(100))
		.add(blockingDateDf, Dimension.percent(100))
		.add(failedRetriesTxt, Dimension.percent(100))
		.add(isActiveChk, Alignment.MIDDLE_LEFT, Dimension.percent(100))
		.add(isBlockedByMaxRetriesChk, Alignment.MIDDLE_LEFT, Dimension.percent(100))
		.addEmptyRow()
		.addCenteredOnNewRow(saveBtn, editBtn, searchBtn)
		.add(userGrid, 2, Dimension.percent(100))
		.add(pager, 2, Dimension.percent(100));
		
		return form;
	}
	
	
	private void addDataBinding(){
		currentUser = new UserDTO();
		BeanItem<UserDTO> beanItem = new BeanItem<UserDTO>(currentUser, UserDTO.class);
		userGridItems = new SortableBeanContainer<UserDTO>(UserDTO.class);
		userGridDS = new GeneratedPropertyContainer(userGridItems);
		fieldGroup = new BeanFieldGroup<UserDTO>(UserDTO.class);
		notificationMsgMapping = new HashMap<String, String>();
		confirmMsgDialogMapping = new HashMap<Button, MessageDialog>();
		eventListener = new AdmUserViewEventListener();
		
		userGridDS.addGeneratedProperty("isBlockedByMaxRetries", new SN2BooleanPropertyGenerator());
		userGridDS.addGeneratedProperty("isActive", new SN2BooleanPropertyGenerator());
		userGridDS.addGeneratedProperty("userId", new PropertyValueGenerator<String>() {
			@Override public String getValue(Item item, Object itemId, Object propertyId) {
				if(propertyId == null) return null;
				return "Roles";
			}
			@Override public Class<String> getType() {
				return String.class;
			}
		});
		isActiveChk.setConverter(new SN2BooleanConverter());
		isBlockedByMaxRetriesChk.setConverter(new SN2BooleanConverter());
		
		userGrid.setContainerDataSource(userGridDS);
		
		beanItem.addNestedProperty("credentials.password");
		
		fieldGroup.setItemDataSource(beanItem);
		fieldGroup.setBuffered(false);
		fieldGroup.bindMemberFields(this);
		
		pager.setPageDataTargetForGrid(userGrid);
	}
	
	@Init
	public void init(){		
		failedRetriesTxt.withFullWidth();
		isActiveChk.setWidth(100, Unit.PERCENTAGE);
		isBlockedByMaxRetriesChk.setWidth(100, Unit.PERCENTAGE);
		
		lastAccessIPTxt.readOnly();
		isoLanguajeCodeTxt.readOnly();
		failedRetriesTxt.readOnly();
		lastAccessDateDf.readOnly();
		blockingDateDf.readOnly();
		
		loginAliasTxt
		.required()
		.withoutUpperTransform();
		identificationNumberTxt.required()
		.withoutUpperTransform();
		fullNameTxt.required();
		emailAddressTxt.withoutUpperTransform();
		isoLanguajeCodeTxt.withoutUpperTransform();
		
		saveBtn.withData("create").withIcon(FontAwesome.SAVE);
		editBtn.withData("edit")
		.withIcon(FontAwesome.EDIT)
		.disable();
		searchBtn.withIcon(FontAwesome.SEARCH);
		
		rolesWindow.setData("rolesAssignement");
		
		userGrid.setColumns(
				"loginAlias",
				"identificationNumber",
				"fullName",
				"emailAddress",
				"isBlockedByMaxRetries",
				"isActive",
				"userId");
		userGrid.getColumn("isActive").setRenderer(new ImageRenderer());
		userGrid.getColumn("isBlockedByMaxRetries").setRenderer(new ImageRenderer());
		userGrid.getColumn("userId").setRenderer(new ButtonRenderer(eventListener));
		userGrid.setCellStyleGenerator(new GridUtils.SimpleCellStyleGenerator(
				new GridUtils.SimpleStyleInfo("isBlockedByMaxRetries", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("isActive", UIConstants.CENTER_ALIGN_STYLE)
				));
		
		userGrid.setHeightMode(HeightMode.ROW);
		userGrid.setHeightByRows(10);
		
		notificationMsgMapping.put(saveBtn.getData().toString(), getText("common.message.success_complete_creation.notification"));
		notificationMsgMapping.put(editBtn.getData().toString(), getText("common.message.success_complete_edition.notification"));
		notificationMsgMapping.put(rolesWindow.getData().toString(), getText("common.message.success_complete_information_creation.notification"));

		confirmMsgDialogMapping.put(saveBtn, new MessageDialog(getText("common.message.creation_confirmation.title"), 
				getText("common.message.creation_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(editBtn, new MessageDialog(getText("common.message.edition_confirmation.title"), 
				getText("common.message.edition_confirmation.question"), Type.QUESTION));
		
		for(Button button : confirmMsgDialogMapping.keySet())
			button.addClickListener(eventListener);
		
	}
	
	private class AdmUserViewEventListener implements ClickListener, RendererClickListener{
		@Override public void buttonClick(ClickEvent event) {
			if(confirmMsgDialogMapping.containsKey(event.getButton()))
				confirmMsgDialogMapping.get(event.getButton()).show();
		}
		
		@Override public void click(RendererClickEvent event) {
			rolesWindow.show((UserDTO) event.getItemId());
		}
		
	}
}
