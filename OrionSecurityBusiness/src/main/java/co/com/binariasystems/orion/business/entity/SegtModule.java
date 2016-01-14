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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alexander
 */
@Entity
@Table(name = "SEGT_MODULOS")
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
    @ManyToOne
    private SegtModule parentModule;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION")
    @ManyToOne(optional = false)
    private SegtApplication applicationId;

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
}