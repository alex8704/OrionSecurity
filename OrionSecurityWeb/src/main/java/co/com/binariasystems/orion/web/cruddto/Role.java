package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;

import co.com.binariasystems.fmw.entity.annot.CRUDViewConfig;
import co.com.binariasystems.fmw.entity.annot.Column;
import co.com.binariasystems.fmw.entity.annot.Entity;
import co.com.binariasystems.fmw.entity.annot.Key;
import co.com.binariasystems.fmw.entity.annot.Relation;
import co.com.binariasystems.fmw.entity.annot.SearcherConfig;
import co.com.binariasystems.fmw.entity.annot.ViewFieldConfig;
import co.com.binariasystems.fmw.entity.cfg.EntityConfigUIControl;
import co.com.binariasystems.fmw.entity.cfg.PKGenerationStrategy;

@Entity(table="SEGT_ROLES",pkGenerationStrategy=PKGenerationStrategy.IDENTITY)
@CRUDViewConfig(searcherConfig=@SearcherConfig(descriptionFields="name"))
public class Role implements Serializable {
	@Key(column="ID_ROL")
    private Integer rolId;
	@Column(name="NOMBRE")
    private String name;
	@Column(name="DESCRIPCION")
    private String description;
	@ViewFieldConfig(uiControl=EntityConfigUIControl.COMBOBOX)
	@Relation(column="ID_APLICACION")
    private Application application;
	/**
	 * @return the rolId
	 */
	public Integer getRolId() {
		return rolId;
	}
	/**
	 * @param rolId the rolId to set
	 */
	public void setRolId(Integer rolId) {
		this.rolId = rolId;
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
    
}
