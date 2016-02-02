package co.com.binariasystems.orion.business.predicate;

import co.com.binariasystems.orion.model.dto.RoleDTO;


public class SelectedRolesPredicate extends AbstractPredicate<RoleDTO>{
	@Override
	public boolean isValid(RoleDTO item) {
		return item.isSelected();
	}
}
