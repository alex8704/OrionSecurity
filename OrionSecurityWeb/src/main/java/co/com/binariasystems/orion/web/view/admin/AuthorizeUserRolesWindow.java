package co.com.binariasystems.orion.web.view.admin;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.exception.FMWException;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.MVPUtils;
import co.com.binariasystems.fmw.vweb.mvp.event.UIEvent;
import co.com.binariasystems.fmw.vweb.mvp.eventbus.EventBus;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.fmw.vweb.util.LocaleMessagesUtil;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.RoleBean;
import co.com.binariasystems.orion.business.bean.UserBean;
import co.com.binariasystems.orion.business.predicate.RolesWhereApplicationNotEqualsToPredicate;
import co.com.binariasystems.orion.business.predicate.SelectedRolesPredicate;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.web.uievent.AuthorizeUserRoleWindowEvent;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;
import co.com.binariasystems.orion.web.utils.RoleSelectableColumnGenerator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class AuthorizeUserRolesWindow extends Window {
	private FormPanel form;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder saveBtn;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder cancelBtn;
	private TextFieldBuilder userNameTxt;
	private Table applicationTable;
	private Table roleTable;
	private SortableBeanContainer<ApplicationDTO> applicationDS;
	private SortableBeanContainer<RoleDTO> roleDS;
	private ApplicationDTO lastSelectedApplication;
	private List<RoleDTO> assignedRoles;
	private UserDTO user;
	private AuthorizeUserRolesViewEventListener eventListener;
	private Map<Button, MessageDialog> confirmMsgDialogMapping;
	
	@Dependency
	private RoleBean roleBean;
	@Dependency
	private ApplicationBean applicationBean;
	@Dependency
	private UserBean userBean;
	
	@Override
	public void attach() {
		super.attach();
		if(form == null)
			init();
		center();
		onLoad();
	}
	
	@Override
	public void detach() {
		super.detach();
		onUnLoad();
	}
	
	private void init(){
		applicationDS = new SortableBeanContainer<ApplicationDTO>(ApplicationDTO.class);
		roleDS = new SortableBeanContainer<RoleDTO>(RoleDTO.class);
		form = new FormPanel(2);
		userNameTxt = new TextFieldBuilder();
		applicationTable = new Table(null, applicationDS);
		roleTable = new Table(null, roleDS);
		saveBtn = new ButtonBuilder();
		cancelBtn = new ButtonBuilder();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		confirmMsgDialogMapping = new HashMap<Button, MessageDialog>();
		
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponents(saveBtn, cancelBtn);
		
		form.add(buttonsLayout, 2, Alignment.TOP_RIGHT)
		.add(userNameTxt, 2, Dimension.percent(100))
		.add(applicationTable, Dimension.percent(100))
		.add(roleTable, Dimension.percent(100));
		
		userNameTxt.readOnly()
		.withoutUpperTransform();
		
		applicationTable.setVisibleColumns("name");
		
		roleTable.addGeneratedColumn("name", new RoleSelectableColumnGenerator());
		roleTable.setVisibleColumns("name");
		
		saveBtn.withData("save")
		.withIcon(FontAwesome.SAVE);
		cancelBtn.withData("cancel")
		.withIcon(FontAwesome.CLOSE);
		
		
		confirmMsgDialogMapping.put(saveBtn, new MessageDialog(getText("common.message.creation_confirmation.title"), 
				getText("common.message.creation_confirmation.question"), Type.QUESTION));
		confirmMsgDialogMapping.put(cancelBtn, new MessageDialog(getText("common.message.cancelation_confirmation.title"), 
				getText("common.message.cancelation_confirmation.question"), Type.QUESTION));
		
		setWidth(650, Unit.PIXELS);
		setResizable(false);
		setModal(true);
		MVPUtils.applyConventionStringsForView(this, OrionWebConstants.ADMIN_VIEW_MESSAGES);
		try {
			MVPUtils.applyValidatorsForView(this);
			MVPUtils.injectIOCProviderDependencies(this, getClass());
		} catch (ParseException | FMWException e) {
			MessageDialog.showExceptions(e);
		}
		setContent(form);
		bindEvents();
	}
	
	
	private void onUnLoad(){
		applicationTable.select(null);
		applicationTable.removeAllItems();
	}
	
	private void onLoad(){
		applicationDS.addAll(applicationBean.findAll());
		assignedRoles = roleBean.findByAssignedUsers(user);
	}

	private void bindEvents() {
		eventListener = new AuthorizeUserRolesViewEventListener();
		applicationTable.addValueChangeListener(eventListener);
		roleTable.addValueChangeListener(eventListener);
		for(Button button : confirmMsgDialogMapping.keySet())
			button.addClickListener(eventListener);
		for(MessageDialog messageDialog : confirmMsgDialogMapping.values())
			messageDialog.addYesClickListener(eventListener);
	}
	
	public void show(UserDTO user){
		this.user = user;
		UI.getCurrent().addWindow(this);
	}
	
	private void reloadRoleTable(){
		roleTable.removeAllItems();
		if(getApplication() == null)return;
		List<RoleDTO> applicationRoles = roleBean.findByApplication(getApplication());
		for(RoleDTO role : applicationRoles)
			role.setSelected(assignedRoles.contains(role));
		roleTable.addItems(applicationRoles);
	}
	
	private void applicationTableValueChange(){
		refreshAssignedRoles();
		reloadRoleTable();
		lastSelectedApplication = getApplication();
	}
	
	private void refreshAssignedRoles(){
		if(lastSelectedApplication != null)
			CollectionUtils.filter(assignedRoles, new RolesWhereApplicationNotEqualsToPredicate(lastSelectedApplication));
		CollectionUtils.select(roleTable.getItemIds(), new SelectedRolesPredicate(), assignedRoles);
	}
	
	private void saveBtnClick(){
		refreshAssignedRoles();
		userBean.save(user, assignedRoles);
		notifyPopupEvent((String)saveBtn.getData());
	}
	
	private void cancelBtnClick(){
		notifyPopupEvent((String)cancelBtn.getData());
	}
	
	private void notifyPopupEvent(String eventId){
		fireEvent(new AuthorizeUserRoleWindowEvent(eventId));
		close();
	}
	
	private ApplicationDTO getApplication(){
		return (ApplicationDTO)applicationTable.getValue();
	}
	
	private String getText(String key){
		return LocaleMessagesUtil.getLocalizedMessage(OrionWebConstants.ADMIN_VIEW_MESSAGES, key);
	}
	
	protected void fireEvent(UIEvent<?> event){
		EventBus eventBus = IOCHelper.getBean(EventBus.class);
		eventBus.fireEvent(event);
	}
	
	private class AuthorizeUserRolesViewEventListener implements ValueChangeListener, ClickListener{
		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationTable.equals(event.getProperty()))
				applicationTableValueChange();
		}

		@Override public void buttonClick(ClickEvent event) {
			if(confirmMsgDialogMapping.containsKey(event.getButton()))
				confirmMsgDialogMapping.get(event.getButton()).show();
			if(confirmMsgDialogMapping.get(saveBtn).yesButton().equals(event.getButton()))
				saveBtnClick();
			if(confirmMsgDialogMapping.get(cancelBtn).yesButton().equals(event.getButton()))
				cancelBtnClick();
		}
		
	}
}
