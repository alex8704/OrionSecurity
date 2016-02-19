package co.com.binariasystems.orion.business.bean.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.orion.business.bean.RoleBean;
import co.com.binariasystems.orion.business.dao.RoleDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtResource;
import co.com.binariasystems.orion.business.entity.SegtUser;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

@Service
@Transactional
public class RoleBeanImpl implements RoleBean {
	@Autowired
	private RoleDAO dao;
	
	@Override
	public List<RoleDTO> findByAuthorizedResources(ResourceDTO resource) {
		return ObjectUtils.transferProperties(dao.findByAuthorizedResources(ObjectUtils.transferProperties(resource, SegtResource.class)), RoleDTO.class);
	}

	@Override
	public List<RoleDTO> findByApplication(ApplicationDTO application) {
		return ObjectUtils.transferProperties(dao.findByApplication(ObjectUtils.transferProperties(application, SegtApplication.class)), RoleDTO.class);
	}

	@Override
	public List<RoleDTO> findByAssignedUsers(UserDTO user) {
		return ObjectUtils.transferProperties(dao.findByAssignedUsers(ObjectUtils.transferProperties(user, SegtUser.class)), RoleDTO.class);
	}

}
