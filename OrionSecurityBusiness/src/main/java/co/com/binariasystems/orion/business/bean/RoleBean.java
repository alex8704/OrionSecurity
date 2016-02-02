package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;

public interface RoleBean {
	public List<RoleDTO> findByAuthorizedResources(ResourceDTO resource);
	
	public List<RoleDTO> findByApplication(ApplicationDTO application);
}
