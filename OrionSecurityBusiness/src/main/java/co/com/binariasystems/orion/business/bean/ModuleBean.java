package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;

public interface ModuleBean {
	public List<ModuleDTO> findByApplicationAndParentModule(ApplicationDTO application, ModuleDTO module);
}
