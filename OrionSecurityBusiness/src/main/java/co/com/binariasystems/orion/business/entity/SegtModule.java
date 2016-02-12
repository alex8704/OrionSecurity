/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.com.binariasystems.orion.business.utils.OrionBusinessConstants;

/**
 *
 * @author Alexander
 */
@Entity
@Table(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_MODULOS")
@NamedQueries({
    @NamedQuery(name = "SegtModule.findAll", query = "SELECT s FROM SegtModule s")})
public class SegtModule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MODULO")
    private Integer moduleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "NOMBRE")
    private String name;
    @Size(max = 256)
    @Column(name = "DESCRIPCION")
    private String description;
    @JoinColumn(name = "ID_MODULO_PADRE", referencedColumnName = "ID_MODULO")
    @ManyToOne(fetch=FetchType.EAGER)
    private SegtModule parentModule;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION")
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private SegtApplication applicationId;
    @Column(name = "POSICION")
    private Integer index;
    
    public SegtModule(){}
    
    public SegtModule(Integer moduleId){
    	this.moduleId = moduleId;
    }
    
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
    public SegtModule getParentModule() {
        return parentModule;
    }

    /**
     * @param parentModule the parentModule to set
     */
    public void setParentModule(SegtModule parentModule) {
        this.parentModule = parentModule;
    }

    /**
     * @return the applicationId
     */
    public SegtApplication getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(SegtApplication applicationId) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentModule == null) ? 0 : parentModule.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SegtModule))
			return false;
		SegtModule other = (SegtModule) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentModule == null) {
			if (other.parentModule != null)
				return false;
		} else if (!parentModule.equals(other.parentModule))
			return false;
		return true;
	}
}
