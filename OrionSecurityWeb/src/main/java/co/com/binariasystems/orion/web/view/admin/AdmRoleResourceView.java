package co.com.binariasystems.orion.web.view.admin;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmRoleResourceViewController;
import co.com.binariasystems.orion.web.utils.RoleSelectableColumnGenerator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;

@View(url="/rolesByResource", viewStringsByConventions=true, controller=AdmRoleResourceViewController.class)
public class AdmRoleResourceView extends AbstractView {
	private FormPanel 						form;
	private ComboBoxBuilder 				applicationCmb;
	private TreeTable 						moduleTree;
	private Table 							resourceTable;
	private Table 							roleTable;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder 					saveBtn;
	private HorizontalLayout 				tablesLayout;
	
	private ObjectProperty<ApplicationDTO>	applicationCmbProperty;
	private ObjectProperty<ModuleDTO> 		moduleTreeProperty;
	private ObjectProperty<ResourceDTO> 	resourceTableProperty; 
	private ValueChangeListener 			valueChangeListener;
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		tablesLayout = new HorizontalLayout();
		applicationCmb = new ComboBoxBuilder();
		moduleTree = new TreeTable();
		resourceTable = new Table();
		roleTable = new Table();
		saveBtn = new ButtonBuilder();
		addDataBinding();
		
		tablesLayout.addComponent(moduleTree);
		tablesLayout.addComponent(resourceTable);
		tablesLayout.addComponent(roleTable);
		
		form.add(applicationCmb, Dimension.percent(100))
		.add(saveBtn, Alignment. BOTTOM_RIGHT)
		.add(tablesLayout, 2, Dimension.percent(100));
		
		return form;
	}
	
	private void addDataBinding(){
		applicationCmbProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		moduleTreeProperty = new ObjectProperty<ModuleDTO>(null, ModuleDTO.class);
		resourceTableProperty = new ObjectProperty<ResourceDTO>(null, ResourceDTO.class);
		valueChangeListener = new AdmRoleResourceViewValueChangeListener();
		
		applicationCmb.setContainerDataSource(new BeanItemContainer<ApplicationDTO>(ApplicationDTO.class));
		applicationCmb.setItemCaptionPropertyId("name");
		applicationCmb.addValueChangeListener(valueChangeListener);
		
		moduleTree.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		moduleTree.setContainerDataSource(new BeanItemContainer<ModuleDTO>(ModuleDTO.class));
		moduleTree.setVisibleColumns("name");
		moduleTree.addValueChangeListener(valueChangeListener);
		
		roleTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		resourceTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		resourceTable.setContainerDataSource(new SortableBeanContainer<ResourceDTO>(ResourceDTO.class));
		resourceTable.setVisibleColumns("name", "resourcePath");
		resourceTable.addValueChangeListener(valueChangeListener);
		
		roleTable.setContainerDataSource(new SortableBeanContainer<RoleDTO>(RoleDTO.class));
		roleTable.addGeneratedColumn("name", new RoleSelectableColumnGenerator());
		roleTable.setVisibleColumns("name");
	}
	
	@Init
	public void init(){
		tablesLayout.setSpacing(true);
		
		moduleTree.setWidth(100, Unit.PERCENTAGE);
		moduleTree.setSelectable(true);
		
		resourceTable.setWidth(100, Unit.PERCENTAGE);
		resourceTable.setSelectable(true);
		
		roleTable.setWidth(100, Unit.PERCENTAGE);
		roleTable.setEnabled(false);
		
		saveBtn.setIcon(FontAwesome.SAVE);
		saveBtn.setEnabled(false);
	}
	
	private class AdmRoleResourceViewValueChangeListener implements ValueChangeListener{
		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationCmb.equals(event.getProperty()))
				applicationCmbProperty.setValue((ApplicationDTO) event.getProperty().getValue());
			if(moduleTree.equals(event.getProperty()))
				moduleTreeProperty.setValue((ModuleDTO) event.getProperty().getValue());
			if(resourceTable.equals(event.getProperty()))
				resourceTableProperty.setValue((ResourceDTO) event.getProperty().getValue());
		}
		
	}
}
