package co.com.binariasystems.orion.business.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.orion.business.dao.OrionSystemDAO;

@Service
@Transactional
public class OrionSystemBean {
	@Autowired
	private OrionSystemDAO dao;
}
