package co.com.binariasystems.orion.business.bean.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.orion.business.bean.ResourceBean;
import co.com.binariasystems.orion.business.dao.ResourceDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtModule;
import co.com.binariasystems.orion.business.entity.SegtResource;
import co.com.binariasystems.orion.business.entity.SegtRole;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;
import co.com.binariasystems.orion.model.dto.RoleDTO;

@Service
@Transactional
public class ResourceBeanImpl implements ResourceBean {
	@Autowired
	private ResourceDAO dao;
	
	@Override
	public List<ResourceDTO> findByApplicationAndNullModule(ApplicationDTO application) {
		return ObjectUtils.transferProperties(
				dao.findByApplicationAndNullModule(
						ObjectUtils.transferProperties(application, SegtApplication.class)),
						ResourceDTO.class);
	}

	@Override
	public List<ResourceDTO> findByApplicationAndModule(ApplicationDTO application, ModuleDTO module) {
		return ObjectUtils.transferProperties(dao.findByApplicationAndModule(
				ObjectUtils.transferProperties(application, SegtApplication.class), 
				ObjectUtils.transferProperties(module, SegtModule.class)),
				ResourceDTO.class);
	}

	@Override
	public ResourceDTO findById(Integer id) {
		return ObjectUtils.transferProperties(dao.findOne(id), ResourceDTO.class);
	}
	
	@Override
	public ResourceDTO save(ResourceDTO resource) {
		SegtResource entity = resource.getResourceId() == null ? ObjectUtils.transferProperties(resource, SegtResource.class) : dao.findOne(resource.getResourceId());
		if(resource.getResourceId() != null)
			ObjectUtils.transferProperties(resource, entity);
		return ObjectUtils.transferProperties(dao.save(entity), ResourceDTO.class);
	}

	@Override
	public ResourceDTO save(ResourceDTO resource, List<RoleDTO> roles) {
		SegtResource entity = ObjectUtils.transferProperties(resource, SegtResource.class);
		entity.setAuthorizedRoles(ObjectUtils.transferProperties(roles, SegtRole.class));
		return ObjectUtils.transferProperties(dao.save(entity), ResourceDTO.class);
	}

	@Override
	public void delete(ResourceDTO resource) {
		dao.delete(ObjectUtils.transferProperties(resource, SegtResource.class));
	}

}
