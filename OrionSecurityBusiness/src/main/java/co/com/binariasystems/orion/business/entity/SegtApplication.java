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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.com.binariasystems.orion.business.enumerated.Application;

/**
 *
 * @author Alexander
 */
@Entity
@Table(name = "SEGT_APLICACIONES")
@NamedQueries({
    @NamedQuery(name = "SegtApplication.findAll", query = "SELECT s FROM SegtApplication s")})
public class SegtApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_APLICACION")
    private Integer applicationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Enumerated(EnumType.STRING)
    @Column(name = "COD_APLICACION")
    private Application applicationCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "NOMBRE")
    private String name;
    @Size(max = 256)
    @Column(name = "DESCRIPCION")
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
    public Application getApplicationCode() {
        return applicationCode;
    }

    /**
     * @param applicationCode the applicationCode to set
     */
    public void setApplicationCode(Application applicationCode) {
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
