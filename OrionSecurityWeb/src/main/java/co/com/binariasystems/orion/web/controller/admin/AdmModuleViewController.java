package co.com.binariasystems.orion.web.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.UIEventHandler;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.cruddto.Module;
import co.com.binariasystems.orion.web.uievent.AdmResourceViewEvent;
import co.com.binariasystems.orion.web.utils.OrionWebUtils;
import co.com.binariasystems.orion.web.view.admin.AdmResourceView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;

@ViewController
public class AdmModuleViewController extends AbstractViewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdmModuleViewController.class);
	@ViewField private FormPanel 							form;
	@ViewField private TreeTable 							moduleHierarchyTree;
	@ViewField private Table 								resourceTable;
	@ViewField private ObjectProperty<ApplicationDTO> 		applicationProperty;
	@ViewField private ObjectProperty<Module> 				parentModuleProperty;
	@ViewField private BeanItemContainer<ApplicationDTO> 	applicationDS;
	@ViewField private BeanItemContainer<ModuleDTO> 		moduleHierarchyItems;
	@ViewField private ContainerHierarchicalWrapper 		moduleHierarchyDS;
	@ViewField private BeanItemContainer<ResourceDTO> 		resourceTableDS;
	@ViewField private ButtonBuilder 						newModuleBtn, 
															newResourceBtn, 
															editModuleBtn, 
															editResourceBtn, 
															deleteModuleBtn, 
															deleteResourceBtn;
	@ViewField private Map<String, String>					notificationMsgMapping;
	@ViewField private Map<Button, MessageDialog>			confirmMsgDialogMapping;
	
	@Dependency
	private ApplicationBean 								applicationBean;
	@Dependency
	private ModuleBean 										moduleBean;
	@Dependency
	private ResourceBean 									resourceBean;
	
	private ApplicationDTO 									lastSelectedApp;
	private AdmModuleViewValueChangeListener 				valueChangeListener;
	private AdmModuleViewClickListener 						clickListener;
	
	@Init
	public void init(){
		valueChangeListener = new AdmModuleViewValueChangeListener();
		clickListener = new AdmModuleViewClickListener();
		applicationProperty.addValueChangeListener(valueChangeListener);
		parentModuleProperty.addValueChangeListener(valueChangeListener);
		moduleHierarchyTree.addValueChangeListener(valueChangeListener);
		resourceTable.addValueChangeListener(valueChangeListener);
		newModuleBtn.addClickListener(clickListener);
		newResourceBtn.addClickListener(clickListener);
		editModuleBtn.addClickListener(clickListener);
		editResourceBtn.addClickListener(clickListener);
		for(MessageDialog messageDialog : confirmMsgDialogMapping.values())
			messageDialog.addYesClickListener(clickListener);
	}
	
	@OnLoad
	public void onLoad(){
		applicationDS.addAll(applicationBean.findAll());
		form.initFocus();
	}
	
	@OnUnLoad 
	public void onUnload(){
		applicationProperty.setValue(null);
	}
	
	private void applicationValueChange(){
		lastSelectedApp = applicationProperty.getValue();
		reloadModulesTree();
		reloadResourcesTable();
		toggleActionButtonsState();
	}
	
	private void parentModuleValueChange(){
		if((lastSelectedApp == null &&  applicationProperty.getValue() != null) || 
				(lastSelectedApp != null && !lastSelectedApp.equals(applicationProperty.getValue())))
			return;
		reloadModulesTree();
		reloadResourcesTable();
		toggleActionButtonsState();
	}
	
	private void moduleTreeValueChange(){
		reloadResourcesTable();
		toggleActionButtonsState();
	}
	
	private void resourceTableValueChange(){
		toggleActionButtonsState();
	}
	
	private void reloadResourcesTable(){
		resourceTable.removeAllItems();
		if(applicationProperty.getValue() == null)return;
		if(moduleHierarchyTree.getValue() == null && parentModuleProperty.getValue() == null)
			resourceTableDS.addAll(resourceBean.findByApplicationAndNullModule(applicationProperty.getValue()));
		else if(moduleHierarchyTree.getValue() == null && parentModuleProperty.getValue() != null)
			resourceTableDS.addAll(resourceBean.findByApplicationAndModule(applicationProperty.getValue(), 
					ObjectUtils.transferProperties(parentModuleProperty.getValue(), ModuleDTO.class)));
		else if(moduleHierarchyTree.getValue() != null)
			resourceTableDS.addAll(resourceBean.findByApplicationAndModule(applicationProperty.getValue(), (ModuleDTO)moduleHierarchyTree.getValue()));
	}
	
	private void toggleActionButtonsState(){
		newModuleBtn.setEnabled(applicationProperty.getValue() != null);
		newResourceBtn.setEnabled(applicationProperty.getValue() != null);
		editModuleBtn.setEnabled(moduleHierarchyTree.getValue() != null);
		editResourceBtn.setEnabled(resourceTable.getValue() != null);
		deleteModuleBtn.setEnabled(moduleHierarchyTree.getValue() != null);
		deleteResourceBtn.setEnabled(resourceTable.getValue() != null);
	}
	
	private void reloadModulesTree(){
		moduleHierarchyTree.removeAllItems();
		if(applicationProperty.getValue() == null) return;
		ModuleDTO parentModule = ObjectUtils.transferProperties(parentModuleProperty.getValue(), ModuleDTO.class);
		for(ModuleDTO module : moduleBean.findByApplicationAndParentModule(applicationProperty.getValue(), parentModule))
			addModuleTreeItem(module);
	}
	
	private void addModuleTreeItem(ModuleDTO module){
		if(moduleHierarchyDS.containsId(module))return;
		if(module.getParentModule() != null && !moduleHierarchyDS.containsId(module.getParentModule()))
			addModuleTreeItem(module.getParentModule());
		
		moduleHierarchyDS.addItem(module);
		moduleHierarchyTree.setCollapsed(module, true);
		if(module.getParentModule() != null)
			moduleHierarchyDS.setParent(module, module.getParentModule());
	}
	
	@UIEventHandler
	public void onResourceViewEvent(AdmResourceViewEvent event){
		if("cancel".equals(event.getId()))return;
		ResourceDTO resource = event.get("resource", ResourceDTO.class);
		reloadResourcesTable();
		if(resourceTableDS.containsId(resource))
			resourceTable.select(resource);
		showSuccessOperationNotification(newResourceBtn.getData().toString());
	}
	
	private void newModuleBtnClickListener(){
		LOGGER.info("Clicked newModuleBtn");
	}
	private void newResourceBtnClickListener(){
		openAdmResourcePopup(newResourceBtn.getData().toString());
	}
	private void editModuleBtnClickListener(){
		LOGGER.info("Clicked editModuleBtn");
	}
	private void editResourceBtnClickListener(){
		openAdmResourcePopup(editResourceBtn.getData().toString());
	}
	private void deleteModuleBtnClickListener(){
		moduleBean.delete((ModuleDTO)moduleHierarchyTree.getValue());
		showSuccessOperationNotification(deleteModuleBtn.getData().toString());
	}
	private void deleteResourceBtnClickListener(){
		resourceBean.delete((ResourceDTO)resourceTable.getValue());
		showSuccessOperationNotification(deleteResourceBtn.getData().toString());
	}
	
	private void openAdmResourcePopup(String action){
		Map<String, String> requestParameters = new HashMap<String, String>();
		ApplicationDTO application = applicationProperty.getValue();
		ModuleDTO parentModule = ObjectUtils.transferProperties(parentModuleProperty.getValue(), ModuleDTO.class);
		ModuleDTO module = moduleHierarchyTree.getValue() != null ? (ModuleDTO)moduleHierarchyTree.getValue() : parentModule;
		ResourceDTO resource = (ResourceDTO) resourceTable.getValue();
		requestParameters.put("applicationId", String.valueOf(application.getApplicationId()));
		requestParameters.put("moduleId", module != null ? String.valueOf(module.getModuleId()) : null);
		if(!newResourceBtn.getData().equals(action))
			requestParameters.put("resourceId", resource != null ? String.valueOf(resource.getResourceId()) : null);
		OrionWebUtils.modalPopup(OrionWebUtils.getViewURL(AdmResourceView.class), requestParameters);
	}
	
	
	private void showSuccessOperationNotification(String action){
		Notification.show(notificationMsgMapping.get(action), Type.TRAY_NOTIFICATION);
	}
	
	private class AdmModuleViewValueChangeListener implements ValueChangeListener{
		@Override public void valueChange(ValueChangeEvent event) {
			try{
				if(applicationProperty.equals(event.getProperty()))
					applicationValueChange();
				if(parentModuleProperty.equals(event.getProperty()))
					parentModuleValueChange();
				if(moduleHierarchyTree.equals(event.getProperty()))
					moduleTreeValueChange();
				if(resourceTable.equals(event.getProperty()))
					resourceTableValueChange();
			}catch(Exception ex){
				MessageDialog.showExceptions(ex, LOGGER);
			}
		}
	}
	
	private class AdmModuleViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			try{
				if(newModuleBtn.equals(event.getButton()))
					newModuleBtnClickListener();
				if(newResourceBtn.equals(event.getButton()))
					newResourceBtnClickListener();
				if(editModuleBtn.equals(event.getButton()))
					editModuleBtnClickListener();
				if(editResourceBtn.equals(event.getButton()))
					editResourceBtnClickListener();
				if(confirmMsgDialogMapping.get(deleteModuleBtn).yesButton().equals(event.getButton()))
					deleteModuleBtnClickListener();
				if(confirmMsgDialogMapping.get(deleteResourceBtn).yesButton().equals(event.getButton()))
					deleteResourceBtnClickListener();
			}catch(Exception ex){
				MessageDialog.showExceptions(ex, LOGGER);
			}
		}
		
	}
}
