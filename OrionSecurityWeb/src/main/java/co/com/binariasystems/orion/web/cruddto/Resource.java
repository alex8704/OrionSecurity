package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;


public class Resource implements Serializable {
    private Integer resourceId;
    private String resourcePath;
    private String name;
    private String description;
    private Module module;
    private Application application;
    private Integer index;
	/**
	 * @return the resourceId
	 */
	public Integer getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * @return the resourcePath
	 */
	public String getResourcePath() {
		return resourcePath;
	}
	/**
	 * @param resourcePath the resourcePath to set
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the module
	 */
	public Module getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(Module module) {
		this.module = module;
	}
	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}
	/**
	 * @param application the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}
	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}
    
    
}
