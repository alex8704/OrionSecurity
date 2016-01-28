package co.com.binariasystems.orion.web.view.admin;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
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

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;

@View(url="/resources", viewStringsByConventions=true, controller=AdmResourceViewController.class)
public class AdmResourceView extends AbstractView {
	/*
	 * Componentes Graficos
	 */
	private FormPanel form;
	private Table applicationTable;
	private TreeTable moduleTree;
	private Table resourceTable;
	private TextFieldBuilder nameTxt;
	private TextFieldBuilder descriptionTxt;
	private TextFieldBuilder indexTxt;
	private TextField pathTxt;
	@NoConventionString(permitDescription=true)
	private ButtonBuilder saveBtn, editBtn, deleteBtn, cancelBtn;
	private HorizontalLayout tablesLayout;
	
	/*
	 * Data Binding y Otros
	 */
	private BeanItemContainer<ApplicationDTO> applicationTableDS;
	private BeanItemContainer<ModuleDTO> moduleTreeItems;
	private ContainerHierarchicalWrapper moduleTreeDS;
	private BeanItemContainer<ResourceDTO> resourceTableDS;
	
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
		.addCenteredOnNewRow(saveBtn, editBtn, deleteBtn, cancelBtn);
		
		return form;
	}
	
	private void addDataBinding(){
		applicationTableDS = new BeanItemContainer<ApplicationDTO>(ApplicationDTO.class);
		moduleTreeItems = new BeanItemContainer<ModuleDTO>(ModuleDTO.class);
		moduleTreeDS = new ContainerHierarchicalWrapper(moduleTreeItems);
		resourceTableDS = new BeanItemContainer<ResourceDTO>(ResourceDTO.class);
		
		applicationTable.setContainerDataSource(applicationTableDS);
		applicationTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		moduleTree.setContainerDataSource(moduleTreeDS);
		moduleTree.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		
		resourceTable.setContainerDataSource(resourceTableDS);
		resourceTable.setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
	}
	
	@Init
	public void init(){
		saveBtn.withIcon(FontAwesome.SAVE)
		.disable();
		editBtn.withIcon(FontAwesome.EDIT)
		.disable();
		deleteBtn.withIcon(FontAwesome.TRASH)
		.disable();
		cancelBtn.withIcon(FontAwesome.CLOSE)
		.disable();
		
		tablesLayout.setSpacing(true);
		
		applicationTable.setWidth(100, Unit.PERCENTAGE);
		applicationTable.addGeneratedColumn("name", new ApplicationColumnGenerator());
		applicationTable.setVisibleColumns("name");
		
		moduleTree.setWidth(100, Unit.PERCENTAGE);
		moduleTree.addGeneratedColumn("name", new ModuleColumnGenerator());
		moduleTree.setVisibleColumns("name");
		
		resourceTable.setWidth(100, Unit.PERCENTAGE);
		resourceTable.addGeneratedColumn("name", new ResourceColumnGenerator());
		resourceTable.setVisibleColumns("name");
	}
}
