package co.com.binariasystems.orion.web.controller.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.commonsmodel.enumerated.Application;
import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.FormValidationException;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.fmw.vweb.util.ValidationUtils;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.business.bean.RoleBean;
import co.com.binariasystems.orion.business.predicate.SelectedRolesPredicate;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;

@ViewController
public class AdmRoleResourceViewController extends AbstractViewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdmRoleResourceViewController.class);
	
	@ViewField private FormPanel 						form;
	@ViewField private ComboBoxBuilder 					applicationCmb;
	@ViewField private TreeTable 						moduleTree;
	@ViewField private Table 							resourceTable;
	@ViewField private Table 							roleTable;
	@ViewField private ButtonBuilder 					saveBtn;
	@ViewField private HorizontalLayout 				tablesLayout;
	
	@ViewField private ObjectProperty<ApplicationDTO>	applicationCmbProperty;
	@ViewField private ObjectProperty<ModuleDTO> 		moduleTreeProperty;
	@ViewField private ObjectProperty<ResourceDTO> 		resourceTableProperty; 
	
	@Dependency
	private ApplicationBean								applicationBean;
	@Dependency
	private ModuleBean									moduleBean;
	@Dependency
	private ResourceBean								resourceBean;
	@Dependency
	private RoleBean									roleBean;
	
	private ValueChangeListener							valueChangeListener;
	private ClickListener								clickListener;
	private boolean										isPopupMode;
	
	@Init
	public void init(){
		clickListener = new AdmRoleResourceViewClickListener();
		valueChangeListener = new AdmRoleResourceViewValueChangeListener();
		applicationCmbProperty.addValueChangeListener(valueChangeListener);
		moduleTreeProperty.addValueChangeListener(valueChangeListener);
		resourceTableProperty.addValueChangeListener(valueChangeListener);
		saveBtn.addClickListener(clickListener);
	}
	
	@OnLoad
	public void onLoad(){
		applicationCmb.addItems(applicationBean.findAll());
	}
	
	@OnUnLoad
	public void onUnload(){
		applicationCmb.removeAllItems();
		form.initFocus();
	}
	
	private void applicationCmbValueChange(){
		if(!isPopupMode){
			reloadModuleTree();
			reloadResourceTable();
			reloadRoleTable();
		}
		applicationCmb.setReadOnly(isPopupMode);
	}
	
	private void moduleTreeValueChange(){
		if(!isPopupMode){
			reloadResourceTable();
		}
		moduleTree.setEnabled(!isPopupMode);
	}
	
	private void resourceTableValueChange(){
		List<RoleDTO> resourceRoles = getResource() != null ? roleBean.findByAuthorizedResources(getResource()) : null;
		Collection<RoleDTO> applicationRoles = new LinkedList<RoleDTO>((Collection<RoleDTO>) roleTable.getItemIds());
		for(RoleDTO role : applicationRoles)
			role.setSelected(resourceRoles != null && resourceRoles.contains(role));
		roleTable.removeAllItems();
		roleTable.addItems(applicationRoles);
		resourceTable.setEnabled(!isPopupMode);
		toggleActionButtonsState();
	}
	
	private void reloadModuleTree(){
		moduleTree.removeAllItems();
		if(getApplication() == null) return;
		List<ModuleDTO> modules = moduleBean.findByApplicationAndParentModule(getApplication(), null);
		for(ModuleDTO item : modules)
			addModuleTreeItem(item);
	}
	
	private void reloadResourceTable(){
		resourceTable.removeAllItems();
		if(getApplication() == null)return;
		List<ResourceDTO> resources = (getModule() == null) ? resourceBean.findByApplicationAndNullModule(getApplication()) :
			resourceBean.findByApplicationAndModule(getApplication(), getModule());
		resourceTable.addItems(resources);
	}
	
	private void reloadRoleTable(){
		roleTable.removeAllItems();
		if(getApplication() == null)return;
		roleTable.addItems(roleBean.findByApplication(getApplication()));
	}
	
	private void addModuleTreeItem(ModuleDTO module){
		if(moduleTree.containsId(module))return;
		if(module.getParentModule() != null && !moduleTree.containsId(module.getParentModule()))
			addModuleTreeItem(module.getParentModule());
		
		moduleTree.addItem(module);
		moduleTree.setCollapsed(module, true);
		if(module.getParentModule() != null)
			moduleTree.setParent(module, module.getParentModule());
	}

	private void toggleActionButtonsState(){
		saveBtn.setEnabled(getResource() != null);
		roleTable.setEnabled(getResource() != null);
	}
	
	private void saveBtnClick() throws FormValidationException{
		if(getResource() == null)
			throw new FormValidationException(ValidationUtils.requiredErrorFor(resourceTable.getCaption()));
		List<RoleDTO> selectedRoles = new ArrayList<RoleDTO>();
		CollectionUtils.select(roleTable.getItemIds(), new SelectedRolesPredicate(), selectedRoles);
		resourceBean.save(getResource(), selectedRoles);
		new Notification(FontAwesome.THUMBS_UP.getHtml(), getText("common.message.success_complete_information_creation.notification"), 
				Type.HUMANIZED_MESSAGE, true).show(Page.getCurrent());
	}
	
	
	private ApplicationDTO getApplication(){
		return applicationCmbProperty.getValue();
	}
	
	private ModuleDTO getModule(){
		return moduleTreeProperty.getValue();
	}
	
	private ResourceDTO getResource(){
		return resourceTableProperty.getValue();
	}
	
	private class AdmRoleResourceViewValueChangeListener implements ValueChangeListener{
		@Override public void valueChange(ValueChangeEvent event) {
			try{
				if(applicationCmbProperty.equals(event.getProperty()))
					applicationCmbValueChange();
				if(moduleTreeProperty.equals(event.getProperty()))
					moduleTreeValueChange();
				if(resourceTableProperty.equals(event.getProperty()))
					resourceTableValueChange();
			}catch(Exception ex){
				handleError(ex, LOGGER);
			}
		}
		
	}
	
	private class AdmRoleResourceViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			try{
				if(saveBtn.equals(event.getButton()))
					saveBtnClick();
			}catch(Exception ex){
				handleError(ex, LOGGER);
			}
		}
		
	}
	
	public static void main(String[] args) {
		List<RoleDTO> rolesA = new LinkedList<RoleDTO>();
		List<RoleDTO> rolesB = new LinkedList<RoleDTO>();
		
		RoleDTO role1 = new RoleDTO();
		role1.setRolId(1);
		role1.setName("MI_ROL");
		role1.setDescription("Mi rol");
		role1.setApplication(new ApplicationDTO());
		role1.getApplication().setApplicationCode(Application.GESTPYMESOC);
		role1.getApplication().setName("GPS");
		role1.setSelected(false);
		rolesA.add(role1);
		
		RoleDTO role2 = new RoleDTO();
		role2.setRolId(1);
		role2.setName("MI_ROL2");
		role2.setDescription("Mi rol");
		role2.setApplication(new ApplicationDTO());
		role2.getApplication().setApplicationCode(Application.GESTPYMESOC);
		role2.getApplication().setName("GPS");
		role2.setSelected(false);
		rolesB.add(role2);
		
		Collection<RoleDTO> c = CollectionUtils.union(rolesA, rolesB);
		for(RoleDTO item : c)
			System.out.println(item);
	}
}
