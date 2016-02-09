package co.com.binariasystems.orion.web.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.binariasystems.fmw.security.FMWSecurityException;
import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;
import co.com.binariasystems.orion.business.bean.services.SecurityBean;
import co.com.binariasystems.orion.model.dto.AccessTokenDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.web.security.OrionAuditoryDataProvider;

@Component
public class OrionMenuGenerator {
	@Autowired
	private SecurityBean securityBean;
	@Autowired
	private OrionAuditoryDataProvider auditoryDataProvider;
	
	public List<MenuElement> generateMenuItems(HttpServletRequest httpRequest) throws FMWSecurityException{
		List<MenuElement> menuHierarchy = new ArrayList<MenuElement>();
		List<ResourceDTO> authorizedResources = getAuthorizedResources(httpRequest);
		
		for(ResourceDTO resource : authorizedResources){
			processResourceHierarchy(resource, menuHierarchy);;
		}
		return menuHierarchy;
	}
	
	
	private void processResourceHierarchy(ResourceDTO resource, List<MenuElement> menuHierarchy){
		MenuOption menuOption = new MenuOption(resource.getName(), resource.getDescription(), resource.getResourcePath());
		if(resource.getModule() == null)
			menuHierarchy.add(menuOption);
		else
			processModuleHierarchy(resource.getModule(), menuHierarchy).addChild(menuOption);
			
	}
	
	private MenuModule processModuleHierarchy(ModuleDTO module, List<MenuElement> menuHierarchy){
		MenuModule menuModule = new MenuModule(module.getName(), module.getDescription());
		if(module.getParentModule() != null){
			MenuModule parentModule = processModuleHierarchy(module.getParentModule(), menuHierarchy);
			if(parentModule.getChilds().contains(menuModule))
				menuModule = (MenuModule) parentModule.getChilds().get(parentModule.getChilds().indexOf(menuModule));
			else
				parentModule.addChild(menuModule);
		}
		else if(menuHierarchy.contains(menuModule))
			menuModule = (MenuModule) menuHierarchy.get(menuHierarchy.indexOf(menuModule));
		else
			menuHierarchy.add(menuModule);
		return menuModule; 
	}
	
	private List<ResourceDTO> getAuthorizedResources(HttpServletRequest httpRequest) throws FMWSecurityException{
		AccessTokenDTO accessToken = auditoryDataProvider.getCurrenAuditoryUser(httpRequest);
		return securityBean.findUserResources(accessToken);
	}
	
	
	
}
