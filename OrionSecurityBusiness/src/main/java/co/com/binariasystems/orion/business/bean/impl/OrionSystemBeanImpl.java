package co.com.binariasystems.orion.business.bean.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.orion.business.bean.OrionSystemBean;
import co.com.binariasystems.orion.business.dao.OrionSystemDAO;

@Service
@Transactional
public class OrionSystemBeanImpl implements OrionSystemBean{
	@Autowired
	private OrionSystemDAO dao;
}
