package co.com.binariasystems.orion.business.predicate;

import co.com.binariasystems.orion.model.dto.UserDTO;

public class UserByIdPredicate extends AbstractPredicate<UserDTO> {
	private Integer userId;
	public UserByIdPredicate(Integer userId){
		this.userId = userId;
	}
	
	@Override
	public boolean isValid(UserDTO item) {
		return (userId == null && item.getUserId() == null) ||
				userId != null && userId.equals(item.getUserId());
	}

}
