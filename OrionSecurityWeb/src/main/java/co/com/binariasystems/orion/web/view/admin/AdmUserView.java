package co.com.binariasystems.orion.web.view.admin;

import java.sql.Timestamp;

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
import co.com.binariasystems.fmw.vweb.uicomponet.Pager;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.DateFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.PasswordFieldBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.fmw.vweb.util.GridUtils;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmUserViewController;
import co.com.binariasystems.orion.web.utils.SN2BooleanConverter;
import co.com.binariasystems.orion.web.utils.SN2BooleanPropertyGenerator;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
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
	@PropertyId("identificationTypeCode")
	private ComboBoxBuilder				identificationTypeCodeCmb;
	@PropertyId("isActive")
	private CheckBox					isActiveChk;
	@PropertyId("isBlockedByMaxRetries")
	private CheckBox					isBlockedByMaxRetriesChk;
	
	private	BeanItemContainer<UserDTO>	userGridItems;
	private GeneratedPropertyContainer 	userGridDS;
	private Grid						userGrid;
	private Pager<UserDTO, UserDTO>		pager;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder				saveBtn,
										editBtn,
										searchBtn;
	private HorizontalLayout 			lastFieldsLayout;
	
	private BeanFieldGroup<UserDTO>		fieldGroup;
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		loginAliasTxt = new TextFieldBuilder();
		credentialsTxt = new PasswordFieldBuilder();
		identificationTypeCodeCmb = new ComboBoxBuilder();
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
		lastFieldsLayout = new HorizontalLayout();
		addDataBinding();
		
		
		lastFieldsLayout.addComponent(failedRetriesTxt);
		lastFieldsLayout.addComponent(isActiveChk);
		lastFieldsLayout.addComponent(isBlockedByMaxRetriesChk);
		
		form.add(loginAliasTxt, Dimension.percent(100))
		.add(credentialsTxt, Dimension.percent(100))
		.add(identificationTypeCodeCmb, Dimension.percent(100))
		.add(identificationNumberTxt, Dimension.percent(100))
		.add(fullNameTxt, Dimension.percent(100))
		.add(emailAddressTxt, Dimension.percent(100))
		.add(isoLanguajeCodeTxt, Dimension.percent(100))
		.add(lastAccessIPTxt, Dimension.percent(100))
		.add(lastAccessDateDf, Dimension.percent(100))
		.add(blockingDateDf, Dimension.percent(100))
		.add(lastFieldsLayout, 2, Dimension.percent(100))
		.addEmptyRow()
		.addCenteredOnNewRow(saveBtn, editBtn, searchBtn)
		.addEmptyRow()
		.add(userGrid, 2, Dimension.percent(100))
		.add(pager, 2, Dimension.percent(100));
		
		return form;
	}
	
	
	private void addDataBinding(){
		BeanItem<UserDTO> beanItem = new BeanItem<UserDTO>(new UserDTO(), UserDTO.class);
		userGridItems = new BeanItemContainer<UserDTO>(UserDTO.class);
		userGridDS = new GeneratedPropertyContainer(userGridItems);
		fieldGroup = new BeanFieldGroup<UserDTO>(UserDTO.class);
		
		userGridDS.addGeneratedProperty("isBlockedByMaxRetries", new SN2BooleanPropertyGenerator());
		userGridDS.addGeneratedProperty("isActive", new SN2BooleanPropertyGenerator());
		
		userGrid.setContainerDataSource(userGridDS);
		
		beanItem.addNestedProperty("credentials.password");
		
		fieldGroup.setItemDataSource(beanItem);
		fieldGroup.setBuffered(false);
		fieldGroup.bindMemberFields(this);
		
		pager.setPageDataTargetForGrid(userGrid);
	}
	
	@Init
	public void init(){
		lastFieldsLayout.setSpacing(true);
		lastFieldsLayout.setComponentAlignment(isActiveChk, Alignment.MIDDLE_LEFT);
		lastFieldsLayout.setComponentAlignment(isBlockedByMaxRetriesChk, Alignment.MIDDLE_LEFT);
		
		failedRetriesTxt.withFullWidth();
		isActiveChk.setWidth(100, Unit.PERCENTAGE);
		isBlockedByMaxRetriesChk.setWidth(100, Unit.PERCENTAGE);
		
		isActiveChk.setConverter(new SN2BooleanConverter());
		isBlockedByMaxRetriesChk.setConverter(new SN2BooleanConverter());
		
		lastAccessIPTxt.readOnly();
		isoLanguajeCodeTxt.readOnly();
		failedRetriesTxt.readOnly();
		lastAccessDateDf.readOnly();
		blockingDateDf.readOnly();
		
		loginAliasTxt
		.required()
		.withoutUpperTransform();
		identificationTypeCodeCmb.required();
		identificationNumberTxt.required()
		.withoutUpperTransform();
		fullNameTxt.required();
		emailAddressTxt.withoutUpperTransform();
		isoLanguajeCodeTxt.withoutUpperTransform();
		
		saveBtn.setIcon(FontAwesome.SAVE);
		editBtn.setIcon(FontAwesome.EDIT);
		searchBtn.setIcon(FontAwesome.SEARCH);
		
		userGrid.setColumns("userId",
				"loginAlias",
				"identificationTypeCode",
				"identificationNumber",
				"fullName",
				"lastAccessDate",
				"lastAccessIP",
				"isoLanguageCode",
				"emailAddress",
				"failedRetries",
				"isBlockedByMaxRetries",
				"blockingDate",
				"isActive");
		userGrid.getColumn("lastAccessDate").setRenderer(GridUtils.obtainRendererForType(Timestamp.class));
		userGrid.getColumn("blockingDate").setRenderer(GridUtils.obtainRendererForType(Timestamp.class));
		userGrid.getColumn("isActive").setRenderer(new ImageRenderer());
		userGrid.getColumn("isBlockedByMaxRetries").setRenderer(new ImageRenderer());
		userGrid.setCellStyleGenerator(new GridUtils.SimpleCellStyleGenerator(
				new GridUtils.SimpleStyleInfo("lastAccessDate", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("lastAccessIP", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("isoLanguageCode", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("failedRetries", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("blockingDate", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("isBlockedByMaxRetries", UIConstants.CENTER_ALIGN_STYLE),
				new GridUtils.SimpleStyleInfo("isActive", UIConstants.CENTER_ALIGN_STYLE)
				));
		
	}
}
