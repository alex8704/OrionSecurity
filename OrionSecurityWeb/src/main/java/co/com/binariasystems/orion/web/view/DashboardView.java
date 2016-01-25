package co.com.binariasystems.orion.web.view;

import static co.com.binariasystems.fmw.vweb.uicomponet.builders.Builders.button;
import static co.com.binariasystems.fmw.vweb.uicomponet.builders.Builders.label;
import static co.com.binariasystems.fmw.vweb.uicomponet.builders.Builders.treeMenu;
import co.com.binariasystems.fmw.vweb.mvp.annotation.DashBoard;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View.Root;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.TreeMenu;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.web.controller.DashboardViewController;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

@DashBoard
@Root(contentSetterMethod="setCurrentView")
@View(url="/", controller=DashboardViewController.class, messages=AuthenticationView.MESSAGES_BUNDLE, viewStringsByConventions= true)
public class DashboardView extends AbstractView implements OrionWebConstants {
	public static final String		MESSAGES_BUNDLE = OrionWebConstants.MESSAGES_PACKAGE + "." + MAIN_MESSAGES_FILE;
	private VerticalLayout mainContainer;
	private HorizontalLayout logoAndTrademarkPanel;
	private HorizontalSplitPanel menuDivisorSplitPanel;
	private VerticalSplitPanel centralSplitPanel;
	private HorizontalLayout topPanel;
	private HorizontalLayout accountInfoPanel;
	
	private LabelBuilder welcomeLbl;
	private LabelBuilder currentUserNameLbl;
	private LabelBuilder loginAliasLbl;
	private LabelBuilder netAddressLbl;
	private LabelBuilder authenticationDateLbl;
	private TreeMenu treeMenu;
	private ButtonBuilder logoutBtn;
	
	
	@ViewBuild
	public Component build(){
		mainContainer = new VerticalLayout();
		logoAndTrademarkPanel = new HorizontalLayout();
		menuDivisorSplitPanel = new HorizontalSplitPanel();
		centralSplitPanel = new VerticalSplitPanel();
		accountInfoPanel = new HorizontalLayout();
		topPanel = new HorizontalLayout();
		treeMenu = treeMenu();
		welcomeLbl = label();
		currentUserNameLbl = label();
		loginAliasLbl = label();
		netAddressLbl = label();
		authenticationDateLbl = label();
		logoutBtn = button(FontAwesome.SIGN_OUT);
		
		mainContainer.addComponent(logoAndTrademarkPanel);
		mainContainer.addComponent(menuDivisorSplitPanel);
		
		menuDivisorSplitPanel.setFirstComponent(treeMenu);
		menuDivisorSplitPanel.setSecondComponent(centralSplitPanel);
		
		centralSplitPanel.setFirstComponent(topPanel);
		
		topPanel.addComponent(accountInfoPanel);
		topPanel.addComponent(logoutBtn);
		
		accountInfoPanel.addComponent(welcomeLbl);
		accountInfoPanel.addComponent(currentUserNameLbl);
		accountInfoPanel.addComponent(loginAliasLbl);
		accountInfoPanel.addComponent(label("|"));
		accountInfoPanel.addComponent(netAddressLbl);
		accountInfoPanel.addComponent(label("|"));
		accountInfoPanel.addComponent(authenticationDateLbl);
		
		return mainContainer;
	}
	
	@Init
	public void init(){
		mainContainer.setSizeFull();
		mainContainer.setExpandRatio(menuDivisorSplitPanel, 1.0f);
		
		logoAndTrademarkPanel.setHeight(40, Unit.PIXELS);
		
		menuDivisorSplitPanel.setSplitPosition(250, Unit.PIXELS);
		
		centralSplitPanel.setMaxSplitPosition(25, Unit.PIXELS);
		centralSplitPanel.setLocked(true);
		
		accountInfoPanel.setSpacing(true);
		accountInfoPanel.setMargin(new MarginInfo(false, true, false, true));
		
		topPanel.setWidth(100, Unit.PERCENTAGE);
		//topPanel.setExpandRatio(logoutBtn, 1.0f);
		topPanel.setComponentAlignment(logoutBtn, Alignment.TOP_RIGHT);
		
		currentUserNameLbl.boldStyle();
		loginAliasLbl.boldStyle();
		logoutBtn.withStyleNames(ValoTheme.BUTTON_LINK);
		
		treeMenu.setTitle(OrionBusinessUtils.getApplicationName());
	}
	
	
	
	public void setCurrentView(Component component){
		centralSplitPanel.setSecondComponent(component);
	}
}
