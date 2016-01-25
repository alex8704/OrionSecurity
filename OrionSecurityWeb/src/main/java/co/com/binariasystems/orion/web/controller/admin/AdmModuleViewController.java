package co.com.binariasystems.orion.web.controller.admin;

import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;
import co.com.binariasystems.orion.web.cruddto.Application;
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
	@ViewField private ObjectProperty<Application> applicationDS;
	@ViewField private ObjectProperty<Module> parentModuleDS;
	@ViewField private BeanItemContainer<MenuElement> itemsDataSource;
	@ViewField private ContainerHierarchicalWrapper gridDataSource;
	
	
	private AdmModuleViewValueChangeListener valueChangeListener;
	
	@Init
	public void init(){
		valueChangeListener = new AdmModuleViewValueChangeListener();
		applicationDS.addValueChangeListener(valueChangeListener);
		parentModuleDS.addValueChangeListener(valueChangeListener);
	}
	
	private void applicationValueChange(){
		System.out.println("Cambio Valor de Aplicacion: "+applicationDS.getValue());
	}
	
	private void parentModuleValueChange(){
		System.out.println("Cambio Valor de Modulo: "+parentModuleDS.getValue());
	}
	
	private class AdmModuleViewValueChangeListener implements ValueChangeListener{

		@Override public void valueChange(ValueChangeEvent event) {
			if(applicationDS.equals(event.getProperty()))
				applicationValueChange();
			if(parentModuleDS.equals(event.getProperty()))
				parentModuleValueChange();
		}
		
	}
}
