package co.com.binariasystems.orion.business.bean.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.business.dao.ModuleDAO;
import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtModule;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;

@Service
@Transactional
public class ModuleBeanImpl implements ModuleBean {
	@Autowired
	private ModuleDAO dao;

	@Override
	public List<ModuleDTO> findByApplicationAndParentModule(ApplicationDTO application, ModuleDTO module) {
		return ObjectUtils.transferProperties(
				dao.findByApplicationAndParentModule(
						ObjectUtils.transferProperties(application, SegtApplication.class), 
						ObjectUtils.transferProperties(module, SegtModule.class)), 
				ModuleDTO.class);
	}

	@Override
	public ModuleDTO findById(Integer id) {
		return ObjectUtils.transferProperties(dao.findOne(id), ModuleDTO.class);
	}

	@Override
	public ModuleDTO save(ModuleDTO module) {
		return ObjectUtils.transferProperties(dao.save(ObjectUtils.transferProperties(module, SegtModule.class)), ModuleDTO.class);
	}

	@Override
	public void delete(ModuleDTO module) {
		dao.delete(ObjectUtils.transferProperties(module, SegtModule.class));
	}
}
