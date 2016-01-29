package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;

public interface ResourceBean {
	public List<ResourceDTO> findByApplicationAndNullModule(ApplicationDTO application);
	
	public List<ResourceDTO> findByApplicationAndModule(ApplicationDTO application, ModuleDTO module);
	
	public ResourceDTO findById(Integer id);
	
}
