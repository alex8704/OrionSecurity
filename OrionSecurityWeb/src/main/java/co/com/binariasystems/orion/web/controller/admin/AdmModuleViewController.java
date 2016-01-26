package co.com.binariasystems.orion.web.controller.admin;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.web.cruddto.Module;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.TreeTable;

@ViewController
public class AdmModuleViewController extends AbstractViewController {
	@ViewField private FormPanel form;
	@ViewField private TreeTable adminGrid;
	@ViewField private ObjectProperty<ApplicationDTO> applicationProperty;
	@ViewField private ObjectProperty<Module> parentModuleProperty;
	@ViewField private BeanItemContainer<MenuElement> gridItemsDS;
	@ViewField private BeanItemContainer<ApplicationDTO> applicationDS;
	@ViewField private ContainerHierarchicalWrapper gridDataSource;
	
	@Dependency
	private ApplicationBean applicationBean;
	private AdmModuleViewValueChangeListener valueChangeListener;
	
	@Init
	public void init(){
		valueChangeListener = new AdmModuleViewValueChangeListener();
		applicationProperty.addValueChangeListener(valueChangeListener);
		parentModuleProperty.addValueChangeListener(valueChangeListener);
	}
	
	@OnLoad
	public void onLoad(){
		applicationDS.addAll(applicationBean.findAll());
	}
	
	@OnUnLoad void onUnload(){
		applicationDS.removeAllItems();
		applicationProperty.setValue(null);
	}
	
	private void applicationValueChange(){
		System.out.println("Cambio Valor de Aplicacion: "+applicationProperty.getValue());
	}
	
	private void parentModuleValueChange(){
		System.out.println("Cambio Valor de Modulo: "+parentModuleProperty.getValue());
	}
	
	private class AdmModuleViewValueChangeListener implements ValueChangeListener{

		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationProperty.equals(event.getProperty()))
				applicationValueChange();
			if(parentModuleProperty.equals(event.getProperty()))
				parentModuleValueChange();
		}
		
	}
}
