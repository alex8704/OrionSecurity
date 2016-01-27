package co.com.binariasystems.orion.web.view.admin;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.SearcherField;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.searcher.VCriteriaUtils;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmModuleViewController;
import co.com.binariasystems.orion.web.cruddto.Module;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;

@View(url="/modules", controller=AdmModuleViewController.class, 
viewStringsByConventions= true)
public class AdmModuleView extends AbstractView{
	private FormPanel form;
	private TreeTable moduleHierarchyTree;
	private Table resourceTable;
	private BeanItemContainer<ApplicationDTO> applicationDS;
	private ComboBoxBuilder applicationCmb;
	private SearcherField<Module> parentModuleTxt;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder newModuleBtn, newResourceBtn;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder editModuleBtn, editResourceBtn;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder deleteModuleBtn, deleteResourceBtn;
	private HorizontalLayout moduleActionsPanel;
	private HorizontalLayout resourceActionsPanel;
	
	private BeanItemContainer<ModuleDTO> moduleHierarchyItems;
	private ContainerHierarchicalWrapper moduleHierarchyDS;
	private BeanItemContainer<ResourceDTO> resourceTableDS;
	private ObjectProperty<ApplicationDTO> applicationProperty;
	private ObjectProperty<Module> parentModuleProperty;
	private ObjectProperty<Integer> auxAplicationProperty;
	
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		applicationCmb = new ComboBoxBuilder();
		parentModuleTxt = new SearcherField<Module>(Module.class);
		moduleHierarchyTree = new TreeTable();
		resourceTable = new Table();
		newModuleBtn = new ButtonBuilder();
		newResourceBtn = new ButtonBuilder();
		editModuleBtn = new ButtonBuilder();
		editResourceBtn = new ButtonBuilder();
		deleteModuleBtn = new ButtonBuilder();
		deleteResourceBtn = new ButtonBuilder();
		moduleActionsPanel = new HorizontalLayout();
		resourceActionsPanel = new HorizontalLayout();
		
		moduleActionsPanel.addComponent(newModuleBtn);
		moduleActionsPanel.addComponent(editModuleBtn);
		moduleActionsPanel.addComponent(deleteModuleBtn);
		
		resourceActionsPanel.addComponent(newResourceBtn);
		resourceActionsPanel.addComponent(editResourceBtn);
		resourceActionsPanel.addComponent(deleteResourceBtn);
		
		form.add(applicationCmb, Dimension.percent(100))
		.add(parentModuleTxt, Dimension.percent(100))
		.addCenteredOnNewRow(new LabelBuilder("&nbsp;").withFullWidth())
		.add(moduleActionsPanel, Alignment.BOTTOM_RIGHT)
		.add(resourceActionsPanel, Alignment.BOTTOM_RIGHT)
		.add(moduleHierarchyTree, Dimension.percent(100))
		.add(resourceTable, Dimension.percent(100));
		
		moduleHierarchyTree.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		resourceTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		return form;
	}
	
	@Init
	public void init(){
		applicationDS = new BeanItemContainer<ApplicationDTO>(ApplicationDTO.class);
		moduleHierarchyItems = new BeanItemContainer<ModuleDTO>(ModuleDTO.class);
		moduleHierarchyDS = new ContainerHierarchicalWrapper(moduleHierarchyItems);
		resourceTableDS = new BeanItemContainer<ResourceDTO>(ResourceDTO.class);
		applicationProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		parentModuleProperty = new ObjectProperty<Module>(null, Module.class);
		auxAplicationProperty = new ObjectProperty<Integer>(null, Integer.class);
		
		moduleActionsPanel.setSpacing(true);
		resourceActionsPanel.setSpacing(true);
		
		newModuleBtn.withIcon(FontAwesome.FILE)
		.disable();
		newResourceBtn.withIcon(FontAwesome.EDIT)
		.disable();
		editModuleBtn.withIcon(FontAwesome.TRASH)
		.disable();
		editResourceBtn.withIcon(FontAwesome.FILE)
		.disable();
		deleteModuleBtn.withIcon(FontAwesome.EDIT)
		.disable();
		deleteResourceBtn.withIcon(FontAwesome.TRASH)
		.disable();
		
		moduleHierarchyTree.setContainerDataSource(moduleHierarchyDS);
		moduleHierarchyTree.setVisibleColumns("name");
		moduleHierarchyTree.setSelectable(true);
		moduleHierarchyTree.setMultiSelect(false);
		
		resourceTable.setContainerDataSource(resourceTableDS);
		resourceTable.setVisibleColumns("name", "resourcePath");
		resourceTable.setSelectable(true);
		resourceTable.setMultiSelect(false);
		
		applicationCmb.setContainerDataSource(applicationDS);
		applicationCmb.withProperty(applicationProperty);
		applicationCmb.setItemCaptionPropertyId("name");
		
		applicationProperty.addValueChangeListener(new ValueChangeListener() {
			@Override public void valueChange(ValueChangeEvent event) {
				auxAplicationProperty.setValue(applicationProperty.getValue() != null ? applicationProperty.getValue().getApplicationId() : null);
			}
		});
		
		parentModuleTxt.setPropertyDataSource(parentModuleProperty);
		parentModuleTxt.setCriteria(VCriteriaUtils.equals("applicationId", auxAplicationProperty));

	}
}
