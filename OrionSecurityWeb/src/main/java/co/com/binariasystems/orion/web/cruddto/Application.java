package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;

import co.com.binariasystems.fmw.entity.annot.Column;
import co.com.binariasystems.fmw.entity.annot.Entity;
import co.com.binariasystems.fmw.entity.annot.Key;
import co.com.binariasystems.fmw.entity.annot.SearcherConfig;
import co.com.binariasystems.fmw.entity.cfg.PKGenerationStrategy;
@Entity(table="SEGT_APLICACIONES",pkGenerationStrategy=PKGenerationStrategy.IDENTITY)
@SearcherConfig(descriptionFields="name")
public class Application implements Serializable {
	@Key(column="ID_APLICACION")
    private Integer applicationId;
	@Column(name="COD_APLICACION")
    private co.com.binariasystems.orion.model.enumerated.Application applicationCode;
	@Column(name="NOMBRE")
    private String name;
	@Column(name="DESCRIPCION")
    private String description;
	/**
	 * @return the applicationId
	 */
	public Integer getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	/**
	 * @return the applicationCode
	 */
	public co.com.binariasystems.orion.model.enumerated.Application getApplicationCode() {
		return applicationCode;
	}
	/**
	 * @param applicationCode the applicationCode to set
	 */
	public void setApplicationCode(co.com.binariasystems.orion.model.enumerated.Application applicationCode) {
		this.applicationCode = applicationCode;
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
    
    
}
