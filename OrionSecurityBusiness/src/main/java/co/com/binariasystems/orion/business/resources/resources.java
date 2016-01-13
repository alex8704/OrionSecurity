package co.com.binariasystems.orion.business.resources;


public final class resources {
	/**
	 * co.com.binariasystems.orion.web.resources
	 * @return
	 */
	public static String resourcesPackage(){
		return resources.class.getPackage().getName();
	}
	
	/**
	 * /co/com/binariasystems/orion/business/resources/
	 * @return
	 */
	public static String resourcesPath(){
		return new StringBuilder("/").append(resourcesPackage().replace(".", "/")).append("/").toString();
	}
	
	
	/**
	 * /co/com/binariasystems/orion/business/resources/images/
	 * @return
	 */
	public static String imagesPath(){
		return new StringBuilder(resourcesPath()).append("images/").toString();
	}
	
	/**
	 * /co/com/binariasystems/orion/business/resources/properties/
	 * @return
	 */
	public static String propertiesPath(){
		return new StringBuilder(resourcesPath()).append("properties/").toString();
	}
	
	/**
	 * co.com.binariasystems.orion.business.resources.messages
	 * @return
	 */
	public static String messagesPackage(){
		return new StringBuilder(resourcesPackage()).append(".messages").toString();
	}
	
/**
 * /co/com/binariasystems/orion/business/resources/images/image.extension
 * @param simpleFileName
 * @return
 */
	public static String getImageFilePath(String simpleFileName){
		return imagesPath()+simpleFileName;
	}
	
	/**
	 * /co/com/binariasystems/orion/business/resources/properties/file.properties
	 * @param simpleFileName
	 * @return
	 */
	public static String getPropertyFilePath(String simpleFileName){
		return propertiesPath()+simpleFileName;
	}
	
	/**
	 * co.com.binariasystems.orion.business.resources.messages.messagefile_es_CO
	 * @param simpleFileName
	 * @return
	 */
	public static String getMessageFilePath(String simpleFileName){
		return messagesPackage()+"."+simpleFileName;
	}
	
	/**
	 * /path-on-server/co/com/binariasystems/orion/business/resources/images/image.extension
	 * @param simpleFileName
	 * @return
	 */
	public static String getAbsoluteImageFilePath(String simpleFileName){
		return resources.class.getResource(imagesPath()+simpleFileName).getPath();
	}
	
	/**
	 * /path-on-server/co/com/binariasystems/orion/business/resources/properties/file.properties
	 * @param simpleFileName
	 * @return
	 */
	public static String getAbsolutePropertyFilePath(String simpleFileName){
		return resources.class.getResource(propertiesPath()+simpleFileName).getPath();
	}
}
