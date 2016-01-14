package co.com.binariasystems.orion.business.dao;
import static co.com.binariasystems.orion.business.resources.resources.getPropertyFilePath;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Repository;

import co.com.binariasystems.fmw.dataaccess.db.FMWAbstractDAO;
import co.com.binariasystems.fmw.util.db.DBUtil;
import co.com.binariasystems.orion.business.resources.resources;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;

@Repository
public class OrionSystemDAO extends FMWAbstractDAO{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrionSystemDAO.class);
	private RowCallbackHandler dbValidationCallback;
	private static final String DATAMODEL_TABLES_CREAT_SCRIPT = "db_tables_create.sql";
	private static final String DATAMODEL_CONSTRAINS_CREAT_SCRIPT = "db_constraints_create.sql";
	private static final String DATAMODEL_SEED_CREAT_SCRIPT = "db_seed.sql";
	
	@Qualifier("jdbcTemplate")
	@Autowired
	public void setTemplate(JdbcTemplate jdbcTemplate){
		setJdbcTemplate(jdbcTemplate);
	}
	
	@PostConstruct
	protected void init(){
		setMessagesFilePath(getPropertyFilePath(new StringBuilder(getClass().getSimpleName()).append("_SQL.xml").toString()));
		ensureMessageBundle();
		
		dbValidationCallback = new RowCallbackHandler() {
			@Override public void processRow(ResultSet rs) throws SQLException {
				LOGGER.info("{} DataBase Validation Sucessfull finished. Dummy Value: '{}'", OrionBusinessUtils.getApplicationName(), rs.getString(1));
			}
		};
	}
	
	public boolean validateDataModelCreation(boolean createIfNotExist){
		if(!isDataModelAlreadyCreated()){
			if(!createIfNotExist) return false;
			if(!runDBTablesCreationScript()) return false;
			if(!runDBConstraintsCreationScript()) return false;
			runDBSeedInsertionScript();
		}
		return true;
	}
	
	private boolean isDataModelAlreadyCreated(){
		if(getDataSource() == null) return true;//Responde True para evitar interpretar error de conexion con NO creacionde BD
		String sqlStmt = getString(new StringBuilder(getClass().getSimpleName()).append(".").append("validateDataModelCreation").toString());
		try {
			getJdbcTemplate().query(sqlStmt, dbValidationCallback);
			return true;
		} catch (DataAccessException e) {
			return false;
		}
	}
	
	private boolean runDBTablesCreationScript(){
		String resourcePath = new StringBuilder(resources.resourcesPath())
		.append("install/db/")
		.append(DBUtil.getCurrentDBMS().name().toLowerCase())
		.append("/").append(DATAMODEL_TABLES_CREAT_SCRIPT).toString();
		return runSingleScript(new ClassPathResource(resourcePath, resources.class));
	}
	
	private boolean runDBConstraintsCreationScript(){
		String resourcePath = new StringBuilder(resources.resourcesPath())
		.append("install/db/")
		.append(DBUtil.getCurrentDBMS().name().toLowerCase())
		.append("/").append(DATAMODEL_CONSTRAINS_CREAT_SCRIPT).toString();
		return runSingleScript(new ClassPathResource(resourcePath, resources.class));
	}
	
	private boolean runDBSeedInsertionScript(){
		String resourcePath = new StringBuilder(resources.resourcesPath())
		.append("install/db/")
		.append(DBUtil.getCurrentDBMS().name().toLowerCase())
		.append("/").append(DATAMODEL_SEED_CREAT_SCRIPT).toString();
		return runSingleScript(new ClassPathResource(resourcePath, resources.class));
	}
	
	private boolean runSingleScript(Resource scriptResource){
		try {
			ScriptUtils.executeSqlScript(getDataSource().getConnection(), scriptResource);
			LOGGER.info("Sucessful script '"+scriptResource.getFilename()+"' execution");
		} catch (ScriptException | SQLException e) {
			LOGGER.error("Has ocurred an unexpected error while trying run script file '"+scriptResource.getFilename()+"'.", e);
			return false;
		}
		return true;
	}
	
	
	public boolean isSuperAdminConfigured(){
		return true;
	}
}
