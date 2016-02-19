package co.com.binariasystems.orion.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.entity.util.FMWEntityConstants;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.util.db.DBUtil;
import co.com.binariasystems.fmw.util.di.SpringIOCProvider;
import co.com.binariasystems.fmw.vweb.constants.VWebCommonConstants;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

/**
 * Application Lifecycle Listener implementation class OrionContextListener
 *
 */

public class OrionContextListener implements ServletContextListener, OrionWebConstants{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrionContextListener.class);
	private boolean successfulInitilized;
	//private static final String PARAM_CREATE_DATAMODEL = OrionContextListener.class.getSimpleName()+".createDataModelIfNotExist";

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  {
    	IOCHelper.setProvider(SpringIOCProvider.configure(WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())));
    	DBUtil.init(IOCHelper.getBean(OrionBusinessUtils.getMainDataSourceName(), DataSource.class));
    	Class resourceLoaderClass = IOCHelper.getBean(FMWConstants.DEFAULT_LOADER_CLASS, Class.class);
    	String entitiesStringsFilePath = IOCHelper.getBean(VWebCommonConstants.APP_ENTITIES_MESSAGES_FILE_IOC_KEY, String.class);
    	String entityOperatiosShowSql = IOCHelper.getBean(FMWEntityConstants.ENTITY_OPERATIONS_SHOWSQL_IOC_KEY, String.class);
    	
    	
    	LOGGER.info(FMWConstants.DEFAULT_LOADER_CLASS + ": " + resourceLoaderClass);
    	LOGGER.info(VWebCommonConstants.APP_ENTITIES_MESSAGES_FILE_IOC_KEY + ": " + entitiesStringsFilePath);
    	LOGGER.info(FMWEntityConstants.ENTITY_OPERATIONS_SHOWSQL_IOC_KEY + ": " + entityOperatiosShowSql);
    	
    	successfulInitilized = initializeApplication(sce);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	LOGGER.info("Bajando la aplicaci\u00f3n [{} Ver. {}]", OrionBusinessUtils.getApplicationName(), OrionBusinessUtils.getApplicationVersion());
    }
    
    private boolean initializeApplication(ServletContextEvent sce){
    	LOGGER.info("Inicializando la aplicaci\u00f3n [{} Ver. {}]", OrionBusinessUtils.getApplicationName(), OrionBusinessUtils.getApplicationVersion());
    	return true;
    }
	
}
