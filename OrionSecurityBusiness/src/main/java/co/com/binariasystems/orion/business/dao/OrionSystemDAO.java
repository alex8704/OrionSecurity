package co.com.binariasystems.orion.business.dao;
import static co.com.binariasystems.orion.business.resources.resources.getPropertyFilePath;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.com.binariasystems.fmw.dataaccess.db.FMWAbstractDAO;

@Repository
public class OrionSystemDAO extends FMWAbstractDAO{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrionSystemDAO.class);
	
	@Qualifier("jdbcTemplate")
	@Autowired
	public void setTemplate(JdbcTemplate jdbcTemplate){
		setJdbcTemplate(jdbcTemplate);
	}
	
	@PostConstruct
	protected void init(){
		setMessagesFilePath(getPropertyFilePath(new StringBuilder(getClass().getSimpleName()).append("_SQL.xml").toString()));
		ensureMessageBundle();
	}
	
	public boolean isSuperAdminConfigured(){
		return true;
	}
}
