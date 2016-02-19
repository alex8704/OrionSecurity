package co.com.binariasystems.orion.business.dao;

import java.util.List;

import co.com.binariasystems.fmw.dataaccess.JPABasedDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtResource;
import co.com.binariasystems.orion.business.entity.SegtRole;
import co.com.binariasystems.orion.business.entity.SegtUser;

public interface RoleDAO extends JPABasedDAO<SegtRole, Integer> {
	public List<SegtRole> findByAssignedUsersAndApplication(SegtUser user, SegtApplication application);
	public List<SegtRole> findByAuthorizedResources(SegtResource resource);
	public List<SegtRole> findByApplication(SegtApplication application);
	public List<SegtRole> findByAssignedUsers(SegtUser user);
}
