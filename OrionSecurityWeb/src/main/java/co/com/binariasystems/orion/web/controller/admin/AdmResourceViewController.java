
package co.com.binariasystems.orion.web.controller.admin;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.mvp.event.PopupViewCloseEvent;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.FormValidationException;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.business.bean.RoleBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.uievent.AdmResourceViewEvent;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
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
public class AdmResourceViewController extends AbstractViewController{
	private static final Logger LOGGER = LoggerFactory.getLogger(AdmResourceViewController.class);
	@ViewField private FormPanel 							form;
	@ViewField private Table 								applicationTable;
	@ViewField private TreeTable 							moduleTree;
	@ViewField private Table 								resourceTable;
	@ViewField private ButtonBuilder 						saveBtn, 
															editBtn, 
															reorganizeBtn, 
															deleteBtn, 
															cancelBtn;
	@ViewField private SortableBeanContainer<ApplicationDTO> 	applicationTableDS;
	@ViewField private BeanItemContainer<ModuleDTO> 		moduleTreeItems;
	@ViewField private ContainerHierarchicalWrapper 		moduleTreeDS;
	@ViewField private SortableBeanContainer<ResourceDTO> 		resourceTableDS;
	@ViewField private ObjectProperty<ApplicationDTO>		applicationTableProperty;
	@ViewField private ObjectProperty<ModuleDTO>			moduleTreeeProperty;
	@ViewField private ObjectProperty<ResourceDTO>			resourceTableProperty;
	@ViewField private BeanFieldGroup<ResourceDTO> 			fieldGroup;
	@ViewField private ResourceDTO 							currentResource;
	@ViewField private Map<String, String>					notificationMsgMapping;
	@ViewField private Map<Button, MessageDialog>			confirmMsgDialogMapping;
	
	
	@Dependency
	private ApplicationBean 								applicationBean;
	@Dependency
	private ModuleBean 										moduleBean;
	@Dependency
	private ResourceBean 									resourceBean;
	@Dependency
	private RoleBean 										roleBean;
	
	private AdmResourceViewValueChangeListener 				valueChangeListener;
	private AdmResourceViewClickListener 					clickListener;
	private boolean 										isPopupMode;
	
	
	@Init
	public void init(){
		valueChangeListener = new AdmResourceViewValueChangeListener();
		clickListener = new AdmResourceViewClickListener();
		applicationTableProperty.addValueChangeListener(valueChangeListener);
		moduleTreeeProperty.addValueChangeListener(valueChangeListener);
		resourceTableProperty.addValueChangeListener(valueChangeListener);
		for(MessageDialog messageDialog : confirmMsgDialogMapping.values())
			messageDialog.addYesClickListener(clickListener);
	}
	
	@OnLoad
	public void onLoad(Map<String, String> requestParams){
		popupLoad(requestParams);
		cancelBtn.setVisible(isPopupMode);
		if(!isPopupMode)
			applicationTableDS.addAll(applicationBean.findAll());
		form.initFocus();
	}
	
	private void popupLoad(Map<String, String> requestParams){
		isPopupMode = (Boolean.valueOf(requestParams.get("popup")).booleanValue());
		if(!isPopupMode)return;
		ResourceDTO resource = null;
		ModuleDTO module = null;
		ApplicationDTO application = null;
		if(requestParams.containsKey("resourceId")){
			resource = resourceBean.findById(Integer.valueOf(requestParams.get("resourceId")));
			application = resource.getApplication();
			module = resource.getModule();
		}
		else if(requestParams.containsKey("moduleId")){
			module = moduleBean.findById(Integer.valueOf(requestParams.get("moduleId")));
			application = module.getApplicationId();
		}
		else
			application = applicationBean.findById(Integer.valueOf(requestParams.get("applicationId")));
		
		applicationTableDS.addItem(application);
		applicationTable.select(application);
		if(module != null){
			addModuleTreeItem(module);
			expandModuleTreeItemRecursively(module);
		}
		moduleTree.select(module);
		reloadResourceTable();
		resourceTable.select(resource);
	}
	
	@OnUnLoad
	public void onUnLoad(){
		isPopupMode = false;
		cleanCurrentResource();
		applicationTable.removeAllItems();
	}
	
	private void applicationTableValueChange(ApplicationDTO application){
		if(!isPopupMode){
			reloadModuleTree();
			reloadResourceTable();
		}
		toggleActionButtonsState();
		applicationTable.setEnabled(!isPopupMode);
	}
	
	private void moduleTreeValueChange(ModuleDTO module){
		if(!isPopupMode){
			reloadResourceTable();
		}
		moduleTree.setEnabled(!isPopupMode);
	}
	
	private void resourceTableValueChange(ResourceDTO resource){
		currentResource = resource;
		resetResourceForm();
		toggleActionButtonsState();
		resourceTable.setEnabled(!isPopupMode);
	}
	
	private void reloadModuleTree(){
		moduleTree.removeAllItems();
		if(applicationTableProperty.getValue() == null) return;
		List<ModuleDTO> modules = moduleBean.findByApplicationAndParentModule(applicationTableProperty.getValue(), null);
		for(ModuleDTO item : modules)
			addModuleTreeItem(item);
	}
	
	private void reloadResourceTable(){
		resourceTable.removeAllItems();
		if(applicationTableProperty.getValue() == null)return;
		List<ResourceDTO> resources = (moduleTreeeProperty.getValue() == null) ? resourceBean.findByApplicationAndNullModule(applicationTableProperty.getValue()) :
			resourceBean.findByApplicationAndModule(applicationTableProperty.getValue(), moduleTreeeProperty.getValue());
		resourceTableDS.addAll(resources);
	}
	
	private void resetResourceForm(){
		currentResource = (currentResource == null) ? new ResourceDTO() : currentResource;
		fieldGroup.setItemDataSource(currentResource);
	}
	
	private void toggleActionButtonsState(){
		saveBtn.setEnabled((isPopupMode && currentResource.getResourceId() == null) || (!isPopupMode && applicationTableProperty.getValue() != null));
		editBtn.setEnabled(currentResource.getResourceId() != null);
		reorganizeBtn.setEnabled(currentResource.getResourceId() != null);
		deleteBtn.setEnabled(!isPopupMode && currentResource.getResourceId() != null);
		cancelBtn.setEnabled(isPopupMode);
	}
	
	private void cleanCurrentResource(){
		currentResource = null;
		resetResourceForm();
	}
	
	private void addModuleTreeItem(ModuleDTO module){
		if(moduleTreeDS.containsId(module))return;
		if(module.getParentModule() != null && !moduleTreeDS.containsId(module.getParentModule()))
			addModuleTreeItem(module.getParentModule());
		
		moduleTreeDS.addItem(module);
		moduleTree.setCollapsed(module, true);
		if(module.getParentModule() != null)
			moduleTreeDS.setParent(module, module.getParentModule());
	}
	
	private void expandModuleTreeItemRecursively(ModuleDTO module){
		ModuleDTO treeNode = module;
		while((treeNode = treeNode.getParentModule()) != null)
			moduleTree.setCollapsed(treeNode, false);
	}
	
	private void cancelOKClick(){
		notifyPopupEvent(cancelBtn.getData().toString(), null);
	}
	
	private void saveAndEditBtnClick(Button button) throws FormValidationException{
		form.validate();
		if(button.equals(saveBtn))
			currentResource.setResourceId(null);
			
		currentResource.setApplication(applicationTableProperty.getValue());
		currentResource.setModule(moduleTreeeProperty.getValue());
		ResourceDTO resource = resourceBean.save(currentResource);
		if(!isPopupMode){
			reloadResourceTable();
			resourceTable.select(resource);
			showSuccessOperationNotification(button.getData().toString());
		}
		notifyPopupEvent(button.getData().toString(), resource);
	}
	
	private void deleteBtnClick(){
		resourceBean.delete(currentResource);
		reloadResourceTable();
		showSuccessOperationNotification(deleteBtn.getData().toString());
	}
	
	private void notifyPopupEvent(String eventId, ResourceDTO resource){
		if(!isPopupMode)return;
		fireEvent(new AdmResourceViewEvent(eventId).set("resource", resource));
		fireEvent(new PopupViewCloseEvent(getClass().getSimpleName()));
	}
	
	private void showSuccessOperationNotification(String action){
		Notification.show(notificationMsgMapping.get(action), Type.TRAY_NOTIFICATION);
	}
	
	private ResourceDTO getCurrentResource(){
		return resourceTableProperty.getValue();
	}
	
	private class AdmResourceViewValueChangeListener implements ValueChangeListener {
		@Override public void valueChange(ValueChangeEvent event) {
			try{
				Object value = event.getProperty().getValue();
				boolean isNull = value == null;
				if(applicationTableProperty.equals(event.getProperty()))
					applicationTableValueChange(applicationTableProperty.getValue());
				if(moduleTreeeProperty.equals(event.getProperty()))
					moduleTreeValueChange(moduleTreeeProperty.getValue());
				if(resourceTableProperty.equals(event.getProperty()))
					resourceTableValueChange(isNull ? null : resourceTableProperty.getValue());
			}catch(Exception ex){
				MessageDialog.showExceptions(ex, LOGGER);
			}
		}	
	}
	
	private class AdmResourceViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			try{
				if(confirmMsgDialogMapping.get(saveBtn).yesButton().equals(event.getButton()))
					saveAndEditBtnClick(saveBtn);
				if(confirmMsgDialogMapping.get(editBtn).yesButton().equals(event.getButton()))
					saveAndEditBtnClick(editBtn);
				if(confirmMsgDialogMapping.get(cancelBtn).yesButton().equals(event.getButton()))
					cancelOKClick();
				if(confirmMsgDialogMapping.get(deleteBtn).yesButton().equals(event.getButton()))
					deleteBtnClick();
			}catch(Exception ex){
				MessageDialog.showExceptions(ex, LOGGER);
				toggleActionButtonsState();
			}
		}
		
	}
}
