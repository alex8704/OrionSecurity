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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Alexander
 */
@Entity
@Table(name = "SEGT_ROLES")
@NamedQueries({
    @NamedQuery(name = "SegtRole.findAll", query = "SELECT s FROM SegtRole s")})
public class SegtRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ROL")
    private Integer rolId;
    @Size(max = 45)
    @Column(name = "NOMBRE")
    private String name;
    @Size(max = 256)
    @Column(name = "DESCRIPCION")
    private String description;
    @ManyToMany(mappedBy = "authorizedRoles")
    private List<SegtResource> authorizedResources;
    @JoinTable(name = "SEGT_ROLES_X_USUARIO", joinColumns = {
        @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")})
    @ManyToMany
    private List<SegtUser> assignedUsers;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION")
    @ManyToOne(optional = false)
    private SegtApplication application;

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
     * @return the authorizedResources
     */
    public List<SegtResource> getAuthorizedResources() {
        return authorizedResources;
    }

    /**
     * @param authorizedResources the authorizedResources to set
     */
    public void setAuthorizedResources(List<SegtResource> authorizedResources) {
        this.authorizedResources = authorizedResources;
    }

    /**
     * @return the assignedUsers
     */
    public List<SegtUser> getAssignedUsers() {
        return assignedUsers;
    }

    /**
     * @param assignedUsers the assignedUsers to set
     */
    public void setAssignedUsers(List<SegtUser> assignedUsers) {
        this.assignedUsers = assignedUsers;
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
}
