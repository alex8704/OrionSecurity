package co.com.binariasystems.orion.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.entity.util.FMWEntityConstants;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.util.db.DBUtil;
import co.com.binariasystems.fmw.util.di.SpringIOCProvider;
import co.com.binariasystems.fmw.util.messagebundle.PropertiesManager;
import co.com.binariasystems.fmw.vweb.constants.VWebCommonConstants;
import co.com.binariasystems.orion.business.bean.OrionSystemBean;
import co.com.binariasystems.orion.business.utils.OrionBusinessConstants;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

/**
 * Application Lifecycle Listener implementation class OrionContextListener
 *
 */

public class OrionContextListener implements ServletContextListener, OrionWebConstants{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrionContextListener.class);
	private static final String PARAM_CREATE_DATAMODEL = OrionContextListener.class.getSimpleName()+".createDataModelIfNotExist";

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  {
    	PropertiesManager properties = PropertiesManager.forPath("/configuration.properties");
    	System.setProperty(OrionBusinessConstants.APPLICATION_VERSION_PROPERTY, properties.getString(OrionBusinessConstants.APPLICATION_VERSION_PROPERTY));
    	System.setProperty(OrionBusinessConstants.APPLICATION_NAME_PROPERTY, properties.getString(OrionBusinessConstants.APPLICATION_NAME_PROPERTY));
    	System.setProperty(OrionBusinessConstants.MAIN_DATASOURCE_PROPERTY, properties.getString(OrionBusinessConstants.MAIN_DATASOURCE_PROPERTY));
    	
    	IOCHelper.setProvider(SpringIOCProvider.configure(WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())));
    	DBUtil.init(IOCHelper.getBean(OrionBusinessUtils.getMainDataSourceName(), DataSource.class));
    	Class resourceLoaderClass = IOCHelper.getBean(FMWConstants.APPLICATION_DEFAULT_CLASS_FOR_RESOURCE_LOAD_IOC_KEY, Class.class);
    	String entitiesStringsFilePath = IOCHelper.getBean(VWebCommonConstants.APP_ENTITIES_MESSAGES_FILE_IOC_KEY, String.class);
    	String entityOperatiosShowSql = IOCHelper.getBean(FMWEntityConstants.ENTITY_OPERATIONS_SHOWSQL_IOC_KEY, String.class);
    	
    	
    	LOGGER.info(FMWConstants.APPLICATION_DEFAULT_CLASS_FOR_RESOURCE_LOAD_IOC_KEY + ": " + resourceLoaderClass);
    	LOGGER.info(VWebCommonConstants.APP_ENTITIES_MESSAGES_FILE_IOC_KEY + ": " + entitiesStringsFilePath);
    	LOGGER.info(FMWEntityConstants.ENTITY_OPERATIONS_SHOWSQL_IOC_KEY + ": " + entityOperatiosShowSql);
    	
    	if(isDataModelCreated(sce)){
    		initializeApplication();
    	}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	LOGGER.info("Bajando la Aplicacion [{}]", OrionBusinessUtils.getApplicationName());
    }
    
    private boolean isDataModelCreated(ServletContextEvent sce){
    	OrionSystemBean systemBean = IOCHelper.getBean(OrionSystemBean.class);
    	return systemBean.validateDataModelCreation(Boolean.valueOf(StringUtils.defaultIfBlank(sce.getServletContext().getInitParameter(PARAM_CREATE_DATAMODEL), "false")));
    }
    
    private void initializeApplication(){
    	
    }
	
}
