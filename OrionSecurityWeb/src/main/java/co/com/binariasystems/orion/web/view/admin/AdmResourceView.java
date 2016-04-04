package co.com.binariasystems.orion.web.view.admin;

import java.util.HashMap;
import java.util.Map;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.NullValidator;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.SortableBeanContainer;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmResourceViewController;
import co.com.binariasystems.orion.web.utils.ApplicationColumnGenerator;
import co.com.binariasystems.orion.web.utils.ModuleColumnGenerator;
import co.com.binariasystems.orion.web.utils.ResourceColumnGenerator;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TreeTable;

@View(url="/resources", viewStringsByConventions=true, controller=AdmResourceViewController.class)
public class AdmResourceView extends AbstractView {
	/*
	 * Componentes Graficos
	 */
	private FormPanel 							form;
	@NullValidator
	private Table 								applicationTable;
	private TreeTable 							moduleTree;
	private Table 								resourceTable;
	@PropertyId("name")
	private TextFieldBuilder 					nameTxt;
	@PropertyId("description")
	private TextFieldBuilder 					descriptionTxt;
	@PropertyId("index")
	private TextFieldBuilder 					indexTxt;
	@PropertyId("resourcePath")
	private TextFieldBuilder 					pathTxt;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder 						saveBtn, 
												editBtn, 
												deleteBtn, 
												reorganizeBtn, 
												cancelBtn;
	private HorizontalLayout 					tablesLayout;
	private Map<String, String>					notificationMsgMapping;
	private Map<Button, MessageDialog>			confirmMsgDialogMapping;
	
	/*
	 * Data Binding y Otros
	 */
	private SortableBeanContainer<ApplicationDTO> 	applicationTableDS;
	private BeanItemContainer<ModuleDTO> 		moduleTreeItems;
	private ContainerHierarchicalWrapper 		moduleTreeDS;
	private SortableBeanContainer<ResourceDTO> 		resourceTableDS;
	private BeanFieldGroup<ResourceDTO> 		fieldGroup;
	private ObjectProperty<ApplicationDTO>		applicationTableProperty;
	private ObjectProperty<ModuleDTO>			moduleTreeeProperty;
	private ObjectProperty<ResourceDTO>			resourceTableProperty;
	private AdmResourceViewValueChangeListener	valueChangeListener;
	private AdmResourceViewClickListener		clickListener;
	private ResourceDTO 						currentResource;
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		applicationTable = new Table();
		moduleTree = new TreeTable();
		resourceTable = new Table();
		nameTxt = new TextFieldBuilder();
		descriptionTxt = new TextFieldBuilder();
		indexTxt = new TextFieldBuilder();
		pathTxt = new TextFieldBuilder();
		saveBtn = new ButtonBuilder();
		editBtn = new ButtonBuilder();
		deleteBtn = new ButtonBuilder();
		reorganizeBtn = new ButtonBuilder();
		cancelBtn = new ButtonBuilder();
		tablesLayout = new HorizontalLayout();
		addDataBinding();
		
		tablesLayout.addComponent(applicationTable);
		tablesLayout.addComponent(moduleTree);
		tablesLayout.addComponent(resourceTable);
		
		form.add(tablesLayout, 2, Dimension.percent(100))
		.addCenteredOnNewRow(new LabelBuilder("&nbsp").withFullWidth())
		.add(nameTxt, Dimension.percent(100))
		.add(descriptionTxt, Dimension.percent(100))
		.add(pathTxt, Dimension.percent(100))
		.add(indexTxt, Dimension.percent(100))
		.addCenteredOnNewRow(saveBtn, editBtn, reorganizeBtn, deleteBtn, cancelBtn);
		
		return form;
	}
	
	private void addDataBinding(){
		applicationTableDS = new SortableBeanContainer<ApplicationDTO>(ApplicationDTO.class);
		moduleTreeItems = new BeanItemContainer<ModuleDTO>(ModuleDTO.class);
		moduleTreeDS = new ContainerHierarchicalWrapper(moduleTreeItems);
		resourceTableDS = new SortableBeanContainer<ResourceDTO>(ResourceDTO.class);
		currentResource = new ResourceDTO();
		fieldGroup = new BeanFieldGroup<ResourceDTO>(ResourceDTO.class);
		applicationTableProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		moduleTreeeProperty = new ObjectProperty<ModuleDTO>(null, ModuleDTO.class);
		resourceTableProperty = new ObjectProperty<ResourceDTO>(null, ResourceDTO.class);
		valueChangeListener = new AdmResourceViewValueChangeListener();
		clickListener = new AdmResourceViewClickListener();
		notificationMsgMapping = new HashMap<String, String>();
		confirmMsgDialogMapping = new HashMap<Button, MessageDialog>();
		
		fieldGroup.setItemDataSource(currentResource);
		
		fieldGroup.setBuffered(false);
		fieldGroup.bindMemberFields(this);
		
		applicationTable.setBuffered(false);
		applicationTable.setContainerDataSource(applicationTableDS);
		applicationTable.addValueChangeListener(valueChangeListener);
		applicationTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		moduleTree.setBuffered(false);
		moduleTree.setContainerDataSource(moduleTreeDS);
		moduleTree.addValueChangeListener(valueChangeListener);
		moduleTree.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		resourceTable.setBuffered(false);
		resourceTable.setContainerDataSource(resourceTableDS);
		resourceTable.addValueChangeListener(valueChangeListener);
		resourceTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
	}
	
	@Init
	public void init(){
		saveBtn.withIcon(FontAwesome.SAVE)
		.withData("create").disable();
		editBtn.withIcon(FontAwesome.EDIT)
		.withData("edit").disable();
		deleteBtn.withIcon(FontAwesome.TRASH)
		.withData("delete").disable();
		reorganizeBtn.withIcon(FontAwesome.REORDER)
		.withData("move").disable();
		cancelBtn.withIcon(FontAwesome.CLOSE)
		.withData("cancel").disable();
		
		applicationTable.setRequired(true);
		nameTxt.required()
		.withoutUpperTransform();
		descriptionTxt.required()
		.withoutUpperTransform();
		pathTxt.required()
		.withoutUpperTransform();
		
		tablesLayout.setSpacing(true);
		
		applicationTable.setWidth(100, Unit.PERCENTAGE);
		applicationTable.setPageLength(12);
		applicationTable.addGeneratedColumn("name", new ApplicationColumnGenerator());
		applicationTable.setVisibleColumns("name");
		applicationTable.setSelectable(true);
		
		moduleTree.setWidth(100, Unit.PERCENTAGE);
		moduleTree.setPageLength(12);
		moduleTree.addGeneratedColumn("name", new ModuleColumnGenerator());
		moduleTree.setVisibleColumns("name");
		moduleTree.setSelectable(true);
		
		resourceTable.setWidth(100, Unit.PERCENTAGE);
		resourceTable.setPageLength(12);
		resourceTable.addGeneratedColumn("name", new ResourceColumnGenerator());
		resourceTable.setVisibleColumns("name", "resourcePath");
		resourceTable.setSelectable(true);
		
		notificationMsgMapping.put(saveBtn.getData().toString(), getText("common.message.success_complete_creation.notification"));
		notificationMsgMapping.put(editBtn.getData().toString(), getText("common.message.success_complete_edition.notification"));
		notificationMsgMapping.put(deleteBtn.getData().toString(), getText("common.message.success_complete_deletion.notification"));
		notificationMsgMapping.put(reorganizeBtn.getData().toString(), getText("common.message.success_complete_reordering.notification"));
		
		confirmMsgDialogMapping.put(saveBtn, new MessageDialog(getText("common.message.creation_confirmation.title"), 
				getText("common.message.creation_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(editBtn, new MessageDialog(getText("common.message.edition_confirmation.title"), 
				getText("common.message.edition_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(deleteBtn, new MessageDialog(getText("common.message.deletion_confirmation.title"), 
				getText("common.message.deletion_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(reorganizeBtn, new MessageDialog(getText("common.message.reorderin_confirmation.title"), 
				getText("common.message.reorderin_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(cancelBtn, new MessageDialog(getText("common.message.cancelation_confirmation.title"), 
				getText("common.message.cancelation_confirmation.question"), Type.QUESTION));
		
		for(Button button : confirmMsgDialogMapping.keySet())
			button.addClickListener(clickListener);
	}
	
	private class AdmResourceViewValueChangeListener implements ValueChangeListener{
		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationTable.equals(event.getProperty()))
				applicationTableProperty.setValue((ApplicationDTO) event.getProperty().getValue());
			if(moduleTree.equals(event.getProperty()))
				moduleTreeeProperty.setValue((ModuleDTO) event.getProperty().getValue());
			if(resourceTable.equals(event.getProperty()))
				resourceTableProperty.setValue((ResourceDTO) event.getProperty().getValue());
		}
	}
	
	private class AdmResourceViewClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			if(confirmMsgDialogMapping.containsKey(event.getButton()))
				confirmMsgDialogMapping.get(event.getButton()).show();
		}
		
	}
}
