package co.com.binariasystems.orion.business.bean;

import java.util.List;

import org.springframework.data.domain.Pageable;

import co.com.binariasystems.fmw.util.pagination.ListPage;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

public interface UserBean {
	public ListPage<UserDTO> findAll(int page, int pageSize);
	public ListPage<UserDTO> findAll(UserDTO user, int page, int pageSize);
	public UserDTO save(UserDTO user);
	public UserDTO save(UserDTO user, List<RoleDTO> assignedRoles);
}
