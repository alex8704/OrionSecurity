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
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.model.dto.ResourceDTO;

@Service
@Transactional
public class ResourceBeanImpl implements ResourceBean {
	@Autowired
	private ResourceDAO dao;
	
	@Override
	public List<ResourceDTO> findByApplicationAndNullModule(ApplicationDTO application) {
		return ObjectUtils.transferPropertiesListRecursive(
				dao.findByApplicationAndNullModule(
						ObjectUtils.transferPropertiesRecursive(application, SegtApplication.class)),
						ResourceDTO.class);
	}

	@Override
	public List<ResourceDTO> findByApplicationAndModule(ApplicationDTO application, ModuleDTO module) {
		return ObjectUtils.transferPropertiesListRecursive(dao.findByApplicationAndModule(
				ObjectUtils.transferPropertiesRecursive(application, SegtApplication.class), 
				ObjectUtils.transferPropertiesRecursive(module, SegtModule.class)),
				ResourceDTO.class);
	}

}
