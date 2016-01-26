package co.com.binariasystems.orion.business.bean.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.orion.business.bean.ApplicationBean;
import co.com.binariasystems.orion.business.dao.ApplicationDAO;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;

@Service
@Transactional
public class ApplicationBeanImpl implements ApplicationBean{
	@Autowired
	ApplicationDAO dao;
	@Override
	public List<ApplicationDTO> findAll() {
		return ObjectUtils.transferPropertiesIterableRecursive(dao.findAll(), ApplicationDTO.class);
	}
	
}
