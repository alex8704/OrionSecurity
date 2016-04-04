package co.com.binariasystems.orion.web.view.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.SearcherField;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.searcher.VCriteriaUtils;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmModuleViewController;
import co.com.binariasystems.orion.web.utils.ModuleColumnGenerator;
import co.com.binariasystems.orion.web.utils.ResourceColumnGenerator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
	private FormPanel 							form;
	private TreeTable 							moduleHierarchyTree;
	private Table 								resourceTable;
	private ComboBoxBuilder 					applicationCmb;
	private SearcherField<ModuleDTO> 			parentModuleTxt;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder						newModuleBtn, 
												newResourceBtn,
												editModuleBtn, 
												editResourceBtn,
												deleteModuleBtn,
												deleteResourceBtn;
	private LabelBuilder						moduleOptionsLbl,
												resourceOptionsLbl;
	private HorizontalLayout 					moduleActionsPanel,
												resourceActionsPanel;
	@NoConventionString
	private CreateModuleWindow					editModuleWindow;
	
	/*
	 * Data Binding y Otros
	 */
	private BeanItemContainer<ApplicationDTO> 	applicationDS;
	private BeanItemContainer<ModuleDTO> 		moduleHierarchyItems;
	private ContainerHierarchicalWrapper 		moduleHierarchyDS;
	private SortableBeanContainer<ResourceDTO> 		resourceTableDS;
	private ObjectProperty<ApplicationDTO> 		applicationProperty;
	private ObjectProperty<ModuleDTO>			parentModuleProperty;
	private ObjectProperty<Integer> 			auxAplicationProperty;
	private Map<String, String>					notificationMsgMapping;
	private Map<Button, MessageDialog>			confirmMsgDialogMapping;
	private AdmModuleViewEventListener			eventListener;
	private Action								newResourceAction,
												newModuleAction,
												deleteModuleAction,
												editModuleAction,
												deleteResourceAction,
												editResourceAction;
	
	/**
	 * Crea los diferente componentes de la interfaz de usuario
	 * Y los ubica dentro del formulario
	 * @return
	 */
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		applicationCmb = new ComboBoxBuilder();
		parentModuleTxt = new SearcherField<ModuleDTO>(ModuleDTO.class);
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
		moduleOptionsLbl = new LabelBuilder();
		resourceOptionsLbl = new LabelBuilder();
		editModuleWindow = new CreateModuleWindow();
		addDataBinding();
		
		moduleActionsPanel.addComponent(moduleOptionsLbl);
		moduleActionsPanel.addComponent(new LabelBuilder().withIcon(FontAwesome.LONG_ARROW_RIGHT));
		moduleActionsPanel.addComponent(newModuleBtn);
		moduleActionsPanel.addComponent(editModuleBtn);
		moduleActionsPanel.addComponent(deleteModuleBtn);
		
		resourceActionsPanel.addComponent(resourceOptionsLbl);
		resourceActionsPanel.addComponent(new LabelBuilder().withIcon(FontAwesome.LONG_ARROW_RIGHT));
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
		resourceTableDS = new SortableBeanContainer<ResourceDTO>(ResourceDTO.class);
		applicationProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		parentModuleProperty = new ObjectProperty<ModuleDTO>(null, ModuleDTO.class);
		auxAplicationProperty = new ObjectProperty<Integer>(null, Integer.class);
		notificationMsgMapping = new HashMap<String, String>();
		confirmMsgDialogMapping = new HashMap<Button, MessageDialog>();
		eventListener = new AdmModuleViewEventListener();
		newResourceAction = new Action(conventionCaption("newResourceAction"));
		newModuleAction = new Action(conventionCaption("newModuleAction"));
		deleteModuleAction = new Action(conventionCaption("deleteModuleAction"));
		editModuleAction = new Action(conventionCaption("editModuleAction"));
		deleteResourceAction = new Action(conventionCaption("deleteResourceAction"));
		editResourceAction = new Action(conventionCaption("editResourceAction"));
		
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
		
		newModuleBtn.withIcon(FontAwesome.PLUS)
		.withData("add-module").disable();
		newResourceBtn.withIcon(FontAwesome.PLUS)
		.withData("add-resource").disable();
		editModuleBtn.withIcon(FontAwesome.EDIT)
		.withData("edit-module").disable();
		editResourceBtn.withIcon(FontAwesome.EDIT)
		.withData("edit-resource").disable();
		deleteModuleBtn.withIcon(FontAwesome.TRASH)
		.withData("delete-module").disable();
		deleteResourceBtn.withIcon(FontAwesome.TRASH)
		.withData("delete-resource").disable();
		
		Iterator<Component> it = moduleActionsPanel.iterator();
		while(it.hasNext())
			moduleActionsPanel.setComponentAlignment(it.next(), Alignment.MIDDLE_RIGHT);
		it = resourceActionsPanel.iterator();
		while(it.hasNext())
			resourceActionsPanel.setComponentAlignment(it.next(), Alignment.MIDDLE_RIGHT);
		
		moduleHierarchyTree.setPageLength(12);
		moduleHierarchyTree.addGeneratedColumn("name", new ModuleColumnGenerator());
		moduleHierarchyTree.setVisibleColumns("name");
		moduleHierarchyTree.setSelectable(true);
		moduleHierarchyTree.setMultiSelect(false);
		
		resourceTable.setPageLength(12);
		resourceTable.addGeneratedColumn("name", new ResourceColumnGenerator());
		resourceTable.setVisibleColumns("name", "resourcePath");
		resourceTable.setSelectable(true);
		resourceTable.setMultiSelect(false);
		
		applicationCmb.setItemCaptionPropertyId("name");
		
		notificationMsgMapping.put(newModuleBtn.getData().toString(), getText("common.message.success_complete_creation.notification"));
		notificationMsgMapping.put(newResourceBtn.getData().toString(), getText("common.message.success_complete_creation.notification"));
		notificationMsgMapping.put(editModuleBtn.getData().toString(), getText("common.message.success_complete_edition.notification"));
		notificationMsgMapping.put(editResourceBtn.getData().toString(), getText("common.message.success_complete_edition.notification"));
		notificationMsgMapping.put(deleteModuleBtn.getData().toString(), getText("common.message.success_complete_deletion.notification"));
		notificationMsgMapping.put(deleteResourceBtn.getData().toString(), getText("common.message.success_complete_deletion.notification"));
		
		confirmMsgDialogMapping.put(deleteModuleBtn, new MessageDialog(getText("common.message.deletion_confirmation.title"), 
				getText("common.message.deletion_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(deleteResourceBtn, new MessageDialog(getText("common.message.deletion_confirmation.title"), 
				getText("common.message.deletion_confirmation.question"), Type.QUESTION));
		
		moduleHierarchyTree.addActionHandler(eventListener);
		
		resourceTable.addActionHandler(eventListener);
		
		for(Button button : confirmMsgDialogMapping.keySet())
			button.addClickListener(eventListener);

	}
	
	private Action[] getModuleTreeActions(){
		return new Action[]{newModuleAction, editModuleAction, deleteModuleAction, newResourceAction};
	}
	
	private Action[] getResourceTableActions(){
		return new Action[]{editResourceAction, deleteResourceAction};
	}
	
	class AdmModuleViewEventListener implements ClickListener, Action.Handler{
		@Override public void buttonClick(ClickEvent event) {
			if(confirmMsgDialogMapping.containsKey(event.getButton()))
				confirmMsgDialogMapping.get(event.getButton()).show();
		}

		@Override public Action[] getActions(Object target, Object sender) {
			if(applicationProperty.getValue() == null) return null;
			if(moduleHierarchyTree.equals(sender))
				return target != null ? getModuleTreeActions() : new Action[]{newModuleAction};
			return target != null ? getResourceTableActions() : new Action[]{newResourceAction};
		}

		@Override public void handleAction(Action action, Object sender, Object target) {
			AbstractSelect targetTable = moduleHierarchyTree.equals(sender) ? moduleHierarchyTree : resourceTable;
			targetTable.select(target);
			if(newModuleAction.equals(action))
				newModuleBtn.click();
			if(editModuleAction.equals(action))
				editModuleBtn.click();
			if(deleteModuleAction.equals(action))
				confirmMsgDialogMapping.get(deleteModuleBtn).show();
			if(newResourceAction.equals(action))
				newResourceBtn.click();
			if(editResourceAction.equals(action))
				editResourceBtn.click();
			if(deleteResourceAction.equals(action))
				confirmMsgDialogMapping.get(deleteResourceBtn).show();
		}
	}
}
