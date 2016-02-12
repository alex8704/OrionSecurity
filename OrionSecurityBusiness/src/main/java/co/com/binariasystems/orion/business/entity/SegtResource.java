/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_RECURSOS")
@NamedQueries({
    @NamedQuery(name = "SegtResource.findAll", query = "SELECT s FROM SegtResource s")})
public class SegtResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RECURSO")
    private Integer resourceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "RUTA_RECURSO")
    private String resourcePath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "NOMBRE")
    private String name;
    @Size(max = 256)
    @Column(name = "DESCRIPCION")
    private String description;
    @JoinTable(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_RECURSOS_X_ROLES", joinColumns = {
        @JoinColumn(name = "ID_RECURSO", referencedColumnName = "ID_RECURSO")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL")})
    @ManyToMany
    private List<SegtRole> authorizedRoles;
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID_MODULO")
    @ManyToOne(fetch=FetchType.EAGER)
    private SegtModule module;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION")
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private SegtApplication application;
    @Column(name = "POSICION")
    private Integer index;
    
    public SegtResource() {}

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
     * @return the authorizedRoles
     */
    public List<SegtRole> getAuthorizedRoles() {
        return authorizedRoles;
    }

    /**
     * @param authorizedRoles the authorizedRoles to set
     */
    public void setAuthorizedRoles(List<SegtRole> authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    /**
     * @return the module
     */
    public SegtModule getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(SegtModule module) {
        this.module = module;
    }

    /**
     * @return the application
     */
    public SegtApplication getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(SegtApplication application) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((resourcePath == null) ? 0 : resourcePath.hashCode());
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
		if (!(obj instanceof SegtResource))
			return false;
		SegtResource other = (SegtResource) obj;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
			return false;
		if (resourcePath == null) {
			if (other.resourcePath != null)
				return false;
		} else if (!resourcePath.equals(other.resourcePath))
			return false;
		return true;
	}
    
}
