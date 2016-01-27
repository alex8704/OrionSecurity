package co.com.binariasystems.orion.web.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.cruddto.Module;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;

@ViewController
public class AdmModuleViewController extends AbstractViewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdmModuleViewController.class);
	@ViewField private FormPanel form;
	@ViewField private TreeTable moduleHierarchyTree;
	@ViewField private Table resourceTable;
	@ViewField private ObjectProperty<ApplicationDTO> applicationProperty;
	@ViewField private ObjectProperty<Module> parentModuleProperty;
	@ViewField private BeanItemContainer<ApplicationDTO> applicationDS;
	@ViewField private BeanItemContainer<ModuleDTO> moduleHierarchyItems;
	@ViewField private ContainerHierarchicalWrapper moduleHierarchyDS;
	@ViewField private BeanItemContainer<ResourceDTO> resourceTableDS;
	@ViewField private ButtonBuilder newModuleBtn, newResourceBtn;
	@ViewField private ButtonBuilder editModuleBtn, editResourceBtn;
	@ViewField private ButtonBuilder deleteModuleBtn, deleteResourceBtn;
	
	@Dependency
	private ApplicationBean applicationBean;
	@Dependency
	private ModuleBean moduleBean;
	@Dependency
	private ResourceBean resourceBean;
	
	private ApplicationDTO lastSelectedApp;
	private AdmModuleViewValueChangeListener valueChangeListener;
	private AdmModuleViewClickListener clickListener;
	
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
		deleteModuleBtn.addClickListener(clickListener);
		deleteResourceBtn.addClickListener(clickListener);
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
					ObjectUtils.transferPropertiesRecursive(parentModuleProperty.getValue(), ModuleDTO.class)));
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
		ModuleDTO parentModule = ObjectUtils.transferPropertiesRecursive(parentModuleProperty.getValue(), ModuleDTO.class);
		for(ModuleDTO module : moduleBean.findByApplicationAndParentModule(applicationProperty.getValue(), parentModule))
			addGriItem(module);
	}
	
	private void addGriItem(ModuleDTO module){
		if(moduleHierarchyDS.containsId(module))return;
		if(module.getParentModule() != null && !moduleHierarchyDS.containsId(module.getParentModule()))
			addGriItem(module.getParentModule());
		
		moduleHierarchyDS.addItem(module);
		moduleHierarchyTree.setCollapsed(module, true);
		if(module.getParentModule() != null)
			moduleHierarchyDS.setParent(module, module.getParentModule());
	}
	
	private void newModuleBtnClickListener(){
		LOGGER.info("Clicked newModuleBtn");
	}
	private void newResourceBtnClickListener(){
		LOGGER.info("Clicked newResourceBtn");
	}
	private void editModuleBtnClickListener(){
		LOGGER.info("Clicked editModuleBtn");
	}
	private void editResourceBtnClickListener(){
		LOGGER.info("Clicked editResourceBtn");
	}
	private void deleteModuleBtnClickListener(){
		LOGGER.info("Clicked deleteModuleBtn");
	}
	private void deleteResourceBtnClickListener(){
		LOGGER.info("Clicked deleteResourceBtn");
	}
	
	private class AdmModuleViewValueChangeListener implements ValueChangeListener{
		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationProperty.equals(event.getProperty()))
				applicationValueChange();
			if(parentModuleProperty.equals(event.getProperty()))
				parentModuleValueChange();
			if(moduleHierarchyTree.equals(event.getProperty()))
				moduleTreeValueChange();
			if(resourceTable.equals(event.getProperty()))
				resourceTableValueChange();
		}
	}
	
	private class AdmModuleViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			if(newModuleBtn.equals(event.getSource()))
				newModuleBtnClickListener();
			if(newResourceBtn.equals(event.getSource()))
				newResourceBtnClickListener();
			if(editModuleBtn.equals(event.getSource()))
				editModuleBtnClickListener();
			if(editResourceBtn.equals(event.getSource()))
				editResourceBtnClickListener();
			if(deleteModuleBtn.equals(event.getSource()))
				deleteModuleBtnClickListener();
			if(deleteResourceBtn.equals(event.getSource()))
				deleteResourceBtnClickListener();
		}
		
	}
}
