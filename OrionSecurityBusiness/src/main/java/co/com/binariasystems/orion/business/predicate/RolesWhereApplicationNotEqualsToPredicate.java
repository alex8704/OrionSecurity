package co.com.binariasystems.orion.business.predicate;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;

public class RolesWhereApplicationNotEqualsToPredicate extends AbstractPredicate<RoleDTO> {
	private ApplicationDTO application;
	public RolesWhereApplicationNotEqualsToPredicate(ApplicationDTO application) {
		this.application = application;
	}
	
	@Override public boolean isValid(RoleDTO item) {
		return !item.getApplication().equals(application);
	}

}
