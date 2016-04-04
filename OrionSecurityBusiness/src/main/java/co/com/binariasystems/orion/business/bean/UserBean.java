package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.fmw.business.domain.Page;
import co.com.binariasystems.fmw.business.domain.PageRequest;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

public interface UserBean {
	public Page<UserDTO> findAll(PageRequest pageRequest);
	public Page<UserDTO> findAll(UserDTO user, PageRequest pageRequest);
	public UserDTO save(UserDTO user);
	public UserDTO save(UserDTO user, List<RoleDTO> assignedRoles);
}
