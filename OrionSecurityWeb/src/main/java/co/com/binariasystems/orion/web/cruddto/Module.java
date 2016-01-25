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

@Entity(table="SEGT_MODULOS",pkGenerationStrategy=PKGenerationStrategy.IDENTITY)
@CRUDViewConfig(searcherConfig=@SearcherConfig(descriptionFields="name"))
public class Module implements Serializable {
	@Key(column="ID_MODULO")
    private Integer moduleId;
	@Column(name="NOMBRE")
	@ViewFieldConfig(ommitUpperTransform=true)
    private String name;
	@Column(name="DESCRIPCION")
	@ViewFieldConfig(ommitUpperTransform=true)
    private String description;
	@Relation(column="ID_MODULO_PADRE")
    private Module parentModule;
	@ViewFieldConfig(uiControl=EntityConfigUIControl.COMBOBOX)
	@Relation(column="ID_APLICACION")
    private Application applicationId;
	@Column(name="POSICION")
    private Integer index;
	/**
	 * @return the moduleId
	 */
	public Integer getModuleId() {
		return moduleId;
	}
	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
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
	 * @return the parentModule
	 */
	public Module getParentModule() {
		return parentModule;
	}
	/**
	 * @param parentModule the parentModule to set
	 */
	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}
	/**
	 * @return the applicationId
	 */
	public Application getApplicationId() {
		return applicationId;
	}
	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(Application applicationId) {
		this.applicationId = applicationId;
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
