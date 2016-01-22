package co.com.binariasystems.orion.business.dao;

import co.com.binariasystems.fmw.dataaccess.JPABasedDAO;
import co.com.binariasystems.orion.business.entity.SegtUser;

public interface UserDAO extends JPABasedDAO<SegtUser, Integer>{
	public SegtUser findFirstByLoginAlias(String loginAlias);
}
