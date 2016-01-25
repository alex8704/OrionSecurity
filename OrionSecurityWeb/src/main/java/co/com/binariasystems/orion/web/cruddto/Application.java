package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;

import co.com.binariasystems.fmw.entity.annot.CRUDViewConfig;
import co.com.binariasystems.fmw.entity.annot.Column;
import co.com.binariasystems.fmw.entity.annot.Entity;
import co.com.binariasystems.fmw.entity.annot.Key;
import co.com.binariasystems.fmw.entity.annot.SearcherConfig;
import co.com.binariasystems.fmw.entity.annot.ViewFieldConfig;
import co.com.binariasystems.fmw.entity.cfg.PKGenerationStrategy;
@Entity(table="SEGT_APLICACIONES",pkGenerationStrategy=PKGenerationStrategy.IDENTITY)
@CRUDViewConfig(searcherConfig=@SearcherConfig(descriptionFields="name"))
public class Application implements Serializable {
	@Key(column="ID_APLICACION")
    private Integer applicationId;
	@Column(name="COD_APLICACION")
    private String applicationCode;
	@Column(name="NOMBRE")
	@ViewFieldConfig(ommitUpperTransform=true)
    private String name;
	@Column(name="DESCRIPCION")
	@ViewFieldConfig(ommitUpperTransform=true)
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
	public String getApplicationCode() {
		return applicationCode;
	}
	/**
	 * @param applicationCode the applicationCode to set
	 */
	public void setApplicationCode(String applicationCode) {
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
