package co.com.binariasystems.orion.business.dao;

import co.com.binariasystems.commonsmodel.enumerated.Application;
import co.com.binariasystems.fmw.dataaccess.JPABasedDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;

public interface ApplicationDAO extends JPABasedDAO<SegtApplication, Integer>{
	public SegtApplication findByApplicationCode(Application application);
}
