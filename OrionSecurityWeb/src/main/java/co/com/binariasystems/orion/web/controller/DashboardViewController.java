package co.com.binariasystems.orion.web.controller;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.security.FMWSecurityException;
import co.com.binariasystems.fmw.security.mgt.SecurityManager;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.TreeMenu;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;
import co.com.binariasystems.fmw.vweb.util.VWebUtils;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.web.security.OrionAuditoryDataProvider;
import co.com.binariasystems.orion.web.utils.MenuAction;
import co.com.binariasystems.orion.web.utils.MenuOption;
import co.com.binariasystems.orion.web.utils.OrionMenuGenerator;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@ViewController
public class DashboardViewController extends AbstractViewController{
	@ViewField private TreeMenu treeMenu;
	@ViewField private LabelBuilder currentUserNameLbl;
	@ViewField private LabelBuilder loginAliasLbl;
	@ViewField private LabelBuilder netAddressLbl;
	@ViewField private LabelBuilder authenticationDateLbl;
	@ViewField private ButtonBuilder logoutBtn;
	@Dependency
	private OrionMenuGenerator menuGenerator;
	@Dependency 
	private OrionAuditoryDataProvider auditoryDataProvider;
	@Dependency
	private SecurityManager securityManager;
	
	private ItemClickListener itemClickListener;
	private ClickListener clickListener;
	private MessageDialog logoutConfirmDialog;
	private DateFormat accessDateFmt = new SimpleDateFormat(FMWConstants.TIMESTAMP_NAMEDDAY_FORMAT, VWebUtils.getCurrentUserLocale());
	
	@Init
	public void init(){
		itemClickListener = new DashboardViewItemClickListener();
		clickListener = new DashboardViewClickListener();
		logoutConfirmDialog = new MessageDialog(getText("DashboardView.closeSession.caption"), getText("DashboardView.questionDialog.closeSession"), Type.QUESTION);
		
		treeMenu.addItemClickListener(itemClickListener);
		logoutBtn.addClickListener(clickListener);
		logoutConfirmDialog.addYesClickListener(clickListener);
	}
	
	@OnLoad
	public void onLoad() throws FMWSecurityException{
		AccessTokenDTO accountInfo = auditoryDataProvider.getCurrenAuditoryUser(getVaadinRequest().getHttpServletRequest());
		List<MenuElement> menuOptions = menuGenerator.generateMenuItems(getVaadinRequest().getHttpServletRequest());
		String accessDate = accessDateFmt.format(accountInfo.getUser().getLastAccessDate());
		menuOptions.add(new MenuAction(getText("DashboardView.closeSession.caption"), getText("DashboardView.closeSession.caption"), "DashboardView.closeSession"));
		treeMenu.setItems(menuOptions);
		currentUserNameLbl.setValue(accountInfo.getUser().getFullName());
		loginAliasLbl.setValue(MessageFormat.format("({0})", accountInfo.getUser().getLoginAlias()));
		netAddressLbl.setValue(MessageFormat.format(getText("DashboardView.netAddressLbl.caption"), accountInfo.getUser().getLastAccessIP()));
		authenticationDateLbl.setValue(MessageFormat.format(getText("DashboardView.authenticationDateLbl.caption"),accessDate));
	}
	
	private void treeMenuItemClickListener(ItemClickEvent event){
		if(event.getItemId() instanceof MenuOption){
			MenuOption option = (MenuOption)event.getItemId();
			Page.getCurrent().setUriFragment(option.getPath());
		}else if(event.getItemId() instanceof MenuAction){
			treeMenuHanldeAction((MenuAction)event.getItemId());
		}
	}
	
	private void treeMenuHanldeAction(MenuAction action){
		if("DashboardView.closeSession".equals(action.getActionId()))
			logoutConfirmDialog.show();
	}
	
	private void logoutButtonClick(ClickEvent event){
		logoutConfirmDialog.show();
	}
	
	private void logoutYesButtonClick(ClickEvent event){
		UI.getCurrent().getSession().close();
		Page.getCurrent().setLocation(VWebUtils.getContextPath());
	}
	
	/*----------------------------------------------------------------------------------------------------------------------
	 * Clases Privadas para el manejo de enventos en lugar de clases Anonimas
	 * Y metodos utiles
	 ----------------------------------------------------------------------------------------------------------------------*/
	
	private VaadinServletRequest getVaadinRequest(){
		return (VaadinServletRequest) VaadinService.getCurrentRequest();
	}
	
	
	private class DashboardViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			if(event.getSource().equals(logoutConfirmDialog.yesButton()))
				logoutYesButtonClick(event);
			if(event.getSource().equals(logoutBtn))
				logoutButtonClick(event);
		}
		
	}
	
	private class DashboardViewItemClickListener implements ItemClickListener{
		@Override public void itemClick(ItemClickEvent event) {
			treeMenuItemClickListener(event);
		}
		
	}
	
}
