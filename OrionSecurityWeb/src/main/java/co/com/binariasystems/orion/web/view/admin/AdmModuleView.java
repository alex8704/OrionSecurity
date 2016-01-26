package co.com.binariasystems.orion.web.view.admin;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.View;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewBuild;
import co.com.binariasystems.fmw.vweb.mvp.views.AbstractView;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.SearcherField;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ComboBoxBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.LabelBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.searcher.VCriteriaUtils;
import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.web.controller.admin.AdmModuleViewController;
import co.com.binariasystems.orion.web.cruddto.Module;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeTable;

@View(url="/modules", controller=AdmModuleViewController.class, 
viewStringsByConventions= true)
public class AdmModuleView extends AbstractView{
	private FormPanel form;
	private TreeTable adminGrid;
	private BeanItemContainer<ApplicationDTO> applicationDS;
	private ComboBoxBuilder applicationCmb;
	private SearcherField<Module> parentModuleTxt;
	private BeanItemContainer<MenuElement> gridItemsDS;
	private ContainerHierarchicalWrapper gridDataSource;
	private ObjectProperty<ApplicationDTO> applicationProperty;
	private ObjectProperty<Module> parentModuleProperty;
	private ObjectProperty<Integer> auxAplicationProperty;
	
	
	@ViewBuild
	public Component build(){
		form = new FormPanel(2);
		applicationCmb = new ComboBoxBuilder();
		parentModuleTxt = new SearcherField<Module>(Module.class);
		adminGrid = new TreeTable();
		form.add(applicationCmb, Dimension.percent(100))
		.add(parentModuleTxt, Dimension.percent(100))
		.addCenteredOnNewRow(new LabelBuilder("&nbsp;").withFullWidth())
		.addCenteredOnNewRow(Dimension.percent(100), adminGrid);
		
		return form;
	}
	
	@Init
	public void init(){
		applicationDS = new BeanItemContainer<ApplicationDTO>(ApplicationDTO.class);
		gridItemsDS = new BeanItemContainer<MenuElement>(MenuElement.class);
		gridDataSource = new ContainerHierarchicalWrapper(gridItemsDS);
		applicationProperty = new ObjectProperty<ApplicationDTO>(null, ApplicationDTO.class);
		parentModuleProperty = new ObjectProperty<Module>(null, Module.class);
		auxAplicationProperty = new ObjectProperty<Integer>(null, Integer.class);
		
		adminGrid.setContainerDataSource(gridDataSource);
		adminGrid.setVisibleColumns("caption");
		
		applicationCmb.setContainerDataSource(applicationDS);
		applicationCmb.withProperty(applicationProperty);
		applicationCmb.setItemCaptionPropertyId("name");
		
		applicationProperty.addValueChangeListener(new ValueChangeListener() {
			@Override public void valueChange(ValueChangeEvent event) {
				if(applicationProperty.getValue() != null)
					auxAplicationProperty.setValue(applicationProperty.getValue().getApplicationId());
			}
		});
		
		parentModuleTxt.setPropertyDataSource(parentModuleProperty);
		parentModuleTxt.setCriteria(VCriteriaUtils.equals("applicationId", auxAplicationProperty));
	}
}
