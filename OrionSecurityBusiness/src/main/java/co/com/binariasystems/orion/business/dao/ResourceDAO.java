package co.com.binariasystems.orion.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import co.com.binariasystems.fmw.dataaccess.JPABasedDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtModule;
import co.com.binariasystems.orion.business.entity.SegtResource;
import co.com.binariasystems.orion.business.entity.SegtRole;

public interface ResourceDAO extends JPABasedDAO<SegtResource, Integer> {
	public List<SegtResource> findByAuthorizedRolesAndApplication(SegtRole role, SegtApplication application);
	
	@Query(name="SegtResource.findByAuthorizedRolesListAndApplication")
	public List<SegtResource> findByAuthorizedRolesListAndApplication(List<SegtRole> roles, SegtApplication application);
	
	@Query(name="SegtResource.findByApplicationAndNullModule")
	public List<SegtResource> findByApplicationAndNullModule(SegtApplication application);
	
	@Query(name="SegtResource.findByApplicationAndModule")
	public List<SegtResource> findByApplicationAndModule(SegtApplication application, SegtModule module);
}
