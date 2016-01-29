package co.com.binariasystems.orion.web.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.mvp.event.PopupViewCloseEvent;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.view.admin.AdmResourceView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;

@ViewController
public class AdmResourceViewController extends AbstractViewController{
	@ViewField private FormPanel form;
	@ViewField private Table applicationTable;
	@ViewField private TreeTable moduleTree;
	@ViewField private Table resourceTable;
	@ViewField private TextFieldBuilder nameTxt;
	@ViewField private TextFieldBuilder descriptionTxt;
	@ViewField private TextFieldBuilder indexTxt;
	@ViewField private TextField pathTxt;
	@ViewField private ButtonBuilder saveBtn, editBtn, deleteBtn, cancelBtn;
	
	@ViewField private BeanItemContainer<ApplicationDTO> applicationTableDS;
	@ViewField private BeanItemContainer<ModuleDTO> moduleTreeItems;
	@ViewField private ContainerHierarchicalWrapper moduleTreeDS;
	@ViewField private BeanItemContainer<ResourceDTO> resourceTableDS;
	@ViewField private ResourceDTO currentResource;
	@ViewField private BeanFieldGroup<ResourceDTO> fieldGroup;
	
	@Dependency
	private ApplicationBean applicationBean;
	@Dependency
	private ModuleBean moduleBean;
	@Dependency
	private ResourceBean resourceBean;
	
	private AdmResourceViewValueChangeListener valueChangeListener;
	private AdmResourceViewClickListener clickListener;
	private boolean isPopupMode;
	private Map<String, String> requestParams;
	
	@Init
	public void init(){
		valueChangeListener = new AdmResourceViewValueChangeListener();
		clickListener = new AdmResourceViewClickListener();
		
		applicationTable.addValueChangeListener(valueChangeListener);
		moduleTree.addValueChangeListener(valueChangeListener);
		resourceTable.addValueChangeListener(valueChangeListener);
		cancelBtn.addClickListener(clickListener);
	}
	
	@OnLoad
	public void onLoad(Map<String, String> requestParams){
		this.requestParams = requestParams;
		isPopupMode = (Boolean.valueOf(requestParams.get("popup")).booleanValue());
		ApplicationDTO application = isPopupMode ? applicationBean.findById(Integer.valueOf(requestParams.get("applicationId"))) : null;
		applicationTableDS.addAll(isPopupMode ? Arrays.asList(application) : applicationBean.findAll());
		applicationTable.select(application);
		cancelBtn.setVisible(isPopupMode);
	}
	
	@OnUnLoad
	public void onUnLoad(){
		isPopupMode = false;
		cleanCurrentResource();
		applicationTable.removeAllItems();
	}
	
	private void applicationTableValueChange(ApplicationDTO application){
		moduleTree.removeAllItems();
		if(application == null) return;
		
		boolean isNullModule = StringUtils.isEmpty(requestParams.get("moduleId"));
		ModuleDTO module = isNullModule ? null : moduleBean.findById(Integer.valueOf(requestParams.get("moduleId")));
		List<ModuleDTO> modules = isPopupMode ? (isNullModule ? null : Arrays.asList(module)) :  moduleBean.findByApplicationAndParentModule(application, module);
		if(modules != null){
			for(ModuleDTO item : modules)
				addModuleTreeItem(item);
		}
		
		moduleTree.select(module);
		applicationTable.setEnabled(!isPopupMode);
	}
	
	private void moduleTreeValueChange(ModuleDTO module){
		ApplicationDTO application = (ApplicationDTO)applicationTable.getValue();
		resourceTable.removeAllItems();
		if(application == null)return;
		
		boolean isNullModule = module == null;
		boolean isNewResource = StringUtils.isEmpty(requestParams.get("resourceId"));
		ResourceDTO resource =  isNewResource ? null : resourceBean.findById(Integer.valueOf(requestParams.get("resourceId")));
		List<ResourceDTO> resources = isPopupMode ? (isNewResource ? null : Arrays.asList(resource)) : 
				(isNullModule ? resourceBean.findByApplicationAndNullModule((ApplicationDTO)applicationTable.getValue()) :
					resourceBean.findByApplicationAndModule((ApplicationDTO)applicationTable.getValue(), module));
		if(resources != null)
			resourceTableDS.addAll(resources);
		resourceTable.select(resource);
		moduleTree.setEnabled(!isPopupMode);
	}
	
	private void resourceTableValueChange(ResourceDTO resource){
		currentResource = resource;
		fieldGroup.setItemDataSource(currentResource);
		toggleActionButtonsState();
		resourceTable.setEnabled(!isPopupMode);
	}
	
	private void toggleActionButtonsState(){
		saveBtn.setEnabled(applicationTable.getValue() != null);
		editBtn.setEnabled(resourceTable.getValue() != null);
		deleteBtn.setEnabled(resourceTable.getValue() != null);
	}
	
	private void cleanCurrentResource(){
		for (Object propertyId : fieldGroup.getBoundPropertyIds())
			fieldGroup.getItemDataSource().getItemProperty(propertyId).setValue(null);
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
	
	private class AdmResourceViewValueChangeListener implements ValueChangeListener {
		@Override public void valueChange(ValueChangeEvent event) {
			Object value = event.getProperty().getValue();
			boolean isNull = value == null;
			if(applicationTable.equals(event.getProperty()))
				applicationTableValueChange(isNull ? null : applicationTableDS.getItem(value).getBean());
			if(moduleTree.equals(event.getProperty()))
				moduleTreeValueChange(isNull ? null : moduleTreeItems.getItem(value).getBean());
			if(resourceTable.equals(event.getProperty()))
				resourceTableValueChange(isNull ? null : resourceTableDS.getItem(value).getBean());
		}	
	}
	
	private class AdmResourceViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			if(cancelBtn.equals(event.getSource()))
				eventBus.fireEvent(new PopupViewCloseEvent(AdmResourceView.class.getSimpleName()));
		}
		
	}
}
