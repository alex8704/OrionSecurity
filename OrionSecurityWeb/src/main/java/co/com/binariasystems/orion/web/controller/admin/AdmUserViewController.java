package co.com.binariasystems.orion.web.controller.admin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.exception.FMWUncheckedException;
import co.com.binariasystems.fmw.util.pagination.ListPage;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.UIEventHandler;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.FormValidationException;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.Pager;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.pager.PageChangeEvent;
import co.com.binariasystems.fmw.vweb.uicomponet.pager.PageChangeHandler;
import co.com.binariasystems.fmw.vweb.util.VWebUtils;
import co.com.binariasystems.orion.business.bean.UserBean;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.model.dto.UserDTO;
import co.com.binariasystems.orion.web.uievent.AuthorizeUserRoleWindowEvent;
import co.com.binariasystems.orion.web.view.admin.AuthorizeUserRolesWindow;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@ViewController
public class AdmUserViewController extends AbstractViewController{
	private static final Logger LOGGER = LoggerFactory.getLogger(AdmUserViewController.class);
	@ViewField private FormPanel 					form;	
	@ViewField private SortableBeanContainer<UserDTO>	userGridItems;
	@ViewField private Grid							userGrid;
	@ViewField private Pager<UserDTO, UserDTO>		pager;
	@ViewField private ButtonBuilder				saveBtn,
													editBtn,
													searchBtn;
	@ViewField private BeanFieldGroup<UserDTO>		fieldGroup;
	@ViewField private UserDTO currentUser;
	@ViewField private Map<String, String>				notificationMsgMapping;
	@ViewField private Map<Button, MessageDialog>		confirmMsgDialogMapping;
	@ViewField private AuthorizeUserRolesWindow		rolesWindow;
	
	@Dependency
	private UserBean userBean;
	private AdmUserViewEventListener eventListener;
	
	@Init
	public void init(){
		eventListener = new AdmUserViewEventListener();
		pager.setPageChangeHandler(eventListener);
		userGrid.addSelectionListener(eventListener);
		searchBtn.addClickListener(eventListener);
		for(MessageDialog messageDialog : confirmMsgDialogMapping.values())
			messageDialog.addYesClickListener(eventListener);
	}
	
	@OnLoad
	public void onLoad(){
		loadAllUsers();
		form.initFocus();
	}
	
	@OnUnLoad
	public void onUnload(){
		cleanForm();
		toggleActionButtonsState();
	}
	
	private void loadAllUsers(){
		if(userGrid.getSelectedRow() != null)
			userGrid.deselect(userGrid.getSelectedRow());
		pager.setFilterDto(new UserDTO());
	}
	
	private void cleanForm(){
		VWebUtils.resetBeanItemDS(fieldGroup.getItemDataSource(), null);
	}
	
	private void userGridSelect(){
		cleanForm();
		UserDTO selected = (UserDTO)userGrid.getSelectedRow();
		if(selected != null)
			selected.getCredentials().setPassword(null);
		setCurrentUser(selected);
		toggleActionButtonsState();
	}
	
	private void setCurrentUser(UserDTO user){
		VWebUtils.resetBeanItemDS(fieldGroup.getItemDataSource(), user);
		toggleActionButtonsState();
	}
	
	private ListPage<UserDTO> userGridLoadPage(PageChangeEvent<UserDTO> event){
		return OrionBusinessUtils.pageToListPage(userBean.findAll(event.getFilterDTO(), event.getPageRequest()));
	}
	
	private void saveBtnClick() throws FormValidationException{
		form.validate();
		currentUser.setUserId(null);
		UserDTO saved = userBean.save(currentUser);
		loadAllUsers();
		userGrid.select(saved);
		showSuccessOperationNotification((String)saveBtn.getData());
	}
	
	private void editBtnClick() throws FormValidationException{
		form.validate();
		UserDTO saved = userBean.save(currentUser);
		loadAllUsers();
		userGrid.select(saved);
		showSuccessOperationNotification((String)editBtn.getData());
	}
	
	private void searchBtnClick(){
		pager.setFilterDto(currentUser);
	}
	
	private void toggleActionButtonsState(){
		editBtn.setEnabled(currentUser.getUserId() != null);
	}
	
	private void showSuccessOperationNotification(String action){
		new Notification(FontAwesome.THUMBS_UP.getHtml(), notificationMsgMapping.get(action), Type.TRAY_NOTIFICATION, true).show(Page.getCurrent());
	}
	
	@UIEventHandler
	public void onAuthorizeUserRolesWindowEvent(AuthorizeUserRoleWindowEvent event){
		if("save".equals(event.getId()))
			showSuccessOperationNotification((String)rolesWindow.getData());
	}
	
	
	private class AdmUserViewEventListener implements SelectionListener, ClickListener, PageChangeHandler<UserDTO, UserDTO>{
		@Override public void select(SelectionEvent event) {
			if(userGrid.equals(event.getSource()))
				userGridSelect();
		}

		@Override public ListPage<UserDTO> loadPage(PageChangeEvent<UserDTO> event) throws FMWUncheckedException {
			return userGridLoadPage(event);
		}

		@Override public void buttonClick(ClickEvent event) {
			try{
				if(confirmMsgDialogMapping.get(saveBtn).yesButton().equals(event.getButton()))
					saveBtnClick();
				if(confirmMsgDialogMapping.get(editBtn).yesButton().equals(event.getButton()))
					editBtnClick();
				if(searchBtn.equals(event.getButton()))
					searchBtnClick();
			}catch(Exception ex){
				handleError(ex, LOGGER);
				toggleActionButtonsState();
			}
		}
	}
}
