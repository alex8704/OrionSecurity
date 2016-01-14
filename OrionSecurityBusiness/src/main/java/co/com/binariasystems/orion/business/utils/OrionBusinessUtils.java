package co.com.binariasystems.orion.business.utils;

public class OrionBusinessUtils implements OrionBusinessConstants {
	public static String getApplicationName(){
		return System.getProperty(APPLICATION_NAME_PROPERTY);
	}
	
	public static String getApplicationVersion(){
		return System.getProperty(APPLICATION_VERSION_PROPERTY);
	}
	
	public static String getMainDataSourceName(){
		return System.getProperty(MAIN_DATASOURCE_PROPERTY);
	}
}
