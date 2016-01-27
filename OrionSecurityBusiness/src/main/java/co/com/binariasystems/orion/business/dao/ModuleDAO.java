package co.com.binariasystems.orion.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import co.com.binariasystems.fmw.dataaccess.JPABasedDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtModule;

public interface ModuleDAO extends JPABasedDAO<SegtModule, Integer>{
	
	@Query(name="SegtModule.findByApplicationAndParentModule")
	public List<SegtModule> findByApplicationAndParentModule(SegtApplication application, SegtModule parentModule);
}
