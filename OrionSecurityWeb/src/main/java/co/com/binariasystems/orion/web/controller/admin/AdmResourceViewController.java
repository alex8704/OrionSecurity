package co.com.binariasystems.orion.web.controller.admin;

import java.util.Map;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ContainerHierarchicalWrapper;
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
	
	@Dependency
	private ApplicationBean applicationBean;
	
	@Init
	public void init(){
		
	}
	
	@OnLoad
	public void onLoad(Map<String, String> requestParams){
		System.out.println("Loading AdmResources Params: "+requestParams);
		//applicationTableDS.addAll(applicationBean.findAll());
	}
	
	@OnUnLoad
	public void onUnLoad(){
	}
	
	
}
