package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;

public interface ResourceBean {
	public List<ResourceDTO> findByApplicationAndNullModule(ApplicationDTO application);
	
	public List<ResourceDTO> findByApplicationAndModule(ApplicationDTO application, ModuleDTO module);
	
	public ResourceDTO findById(Integer id);
	
	public ResourceDTO save(ResourceDTO resource);
	
	public ResourceDTO save(ResourceDTO resource, List<RoleDTO> roles);
	
	public void delete(ResourceDTO resource);
	
}
