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
import co.com.binariasystems.orion.web.utils.ModuleColumnGenerator;
import co.com.binariasystems.orion.web.utils.ResourceColumnGenerator;

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
	/*
	 * Componentes Graficos
	 */
	private FormPanel form;
	private TreeTable moduleHierarchyTree;
	private Table resourceTable;
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
	
	/*
	 * Data Binding y Otros
	 */
	private BeanItemContainer<ApplicationDTO> applicationDS;
	private BeanItemContainer<ModuleDTO> moduleHierarchyItems;
	private ContainerHierarchicalWrapper moduleHierarchyDS;
	private BeanItemContainer<ResourceDTO> resourceTableDS;
	private ObjectProperty<ApplicationDTO> applicationProperty;
	private ObjectProperty<Module> parentModuleProperty;
	private ObjectProperty<Integer> auxAplicationProperty;
	
	/**
	 * Crea los diferente componentes de la interfaz de usuario
	 * Y los ubica dentro del formulario
	 * @return
	 */
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
		addDataBinding();
		
		moduleActionsPanel.addComponent(newModuleBtn);
		moduleActionsPanel.addComponent(editModuleBtn);
		moduleActionsPanel.addComponent(deleteModuleBtn);
		
		resourceActionsPanel.addComponent(newResourceBtn);
		resourceActionsPanel.addComponent(editResourceBtn);
		resourceActionsPanel.addComponent(deleteResourceBtn);
		
		form.add(applicationCmb, Dimension.percent(100))
		.add(parentModuleTxt, Dimension.percent(100))
		.addEmptyRow()
		.add(moduleActionsPanel, Alignment.BOTTOM_RIGHT)
		.add(resourceActionsPanel, Alignment.BOTTOM_RIGHT)
		.add(moduleHierarchyTree, Dimension.percent(100))
		.add(resourceTable, Dimension.percent(100));
		
		moduleHierarchyTree.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		resourceTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		return form;
	}
	
	/**
	 * Inicializa todos los objetos encargados del DataBinding
	 * de la Interfaz grafica
	 */
	private void addDataBinding(){
		applicationDS = new BeanItemContainer<ApplicationDTO>(ApplicationDTO.class);
		moduleHierarchyItems = new BeanItemContainer<ModuleDTO>(ModuleDTO.class);
		moduleHierarchyDS = new ContainerHierarchicalWrapper(moduleHierarchyItems);
		resourceTableDS = new BeanItemContainer<ResourceDTO>(ResourceDTO.class);
		applicationProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		parentModuleProperty = new ObjectProperty<Module>(null, Module.class);
		auxAplicationProperty = new ObjectProperty<Integer>(null, Integer.class);
		
		moduleHierarchyTree.setContainerDataSource(moduleHierarchyDS);
		resourceTable.setContainerDataSource(resourceTableDS);
		applicationCmb.setContainerDataSource(applicationDS);
		applicationCmb.withProperty(applicationProperty);
		applicationProperty.addValueChangeListener(new ValueChangeListener() {
			@Override public void valueChange(ValueChangeEvent event) {
				auxAplicationProperty.setValue(applicationProperty.getValue() != null ? applicationProperty.getValue().getApplicationId() : null);
			}
		});
		
		parentModuleTxt.setPropertyDataSource(parentModuleProperty);
		parentModuleTxt.setCriteria(VCriteriaUtils.equals("applicationId", auxAplicationProperty));
	}
	
	/**
	 * Inicializa aspectos de presentacion y organizacion de la
	 * interfaz de usuario, tales como tamanos de elementos, colores de fuentes
	 * distribucion de elementos, etiquetas, etc. Meramente aspectos esteticos
	 */
	@Init
	public void init(){
		moduleActionsPanel.setSpacing(true);
		resourceActionsPanel.setSpacing(true);
		
		newModuleBtn.withIcon(FontAwesome.FILE)
		.disable();
		newResourceBtn.withIcon(FontAwesome.FILE)
		.disable();
		editModuleBtn.withIcon(FontAwesome.EDIT)
		.disable();
		editResourceBtn.withIcon(FontAwesome.EDIT)
		.disable();
		deleteModuleBtn.withIcon(FontAwesome.TRASH)
		.disable();
		deleteResourceBtn.withIcon(FontAwesome.TRASH)
		.disable();
		
		moduleHierarchyTree.addGeneratedColumn("name", new ModuleColumnGenerator());
		moduleHierarchyTree.setVisibleColumns("name");
		moduleHierarchyTree.setSelectable(true);
		moduleHierarchyTree.setMultiSelect(false);
		
		resourceTable.addGeneratedColumn("name", new ResourceColumnGenerator());
		resourceTable.setVisibleColumns("name", "resourcePath");
		resourceTable.setSelectable(true);
		resourceTable.setMultiSelect(false);
		
		applicationCmb.setItemCaptionPropertyId("name");

	}
}
