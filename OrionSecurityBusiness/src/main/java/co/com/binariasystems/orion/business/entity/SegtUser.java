/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;
import co.com.binariasystems.orion.business.utils.OrionBusinessConstants;

/**
 *
 * @author Alexander
 */
@Entity
@Table(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_USUARIOS")
@NamedQueries({
    @NamedQuery(name = "SegtUser.findAll", query = "SELECT s FROM SegtUser s")})
public class SegtUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "LOGIN_ALIAS")
    private String loginAlias;
    @Embedded
    private SegtUserCredentials credentials;
    @Size(max = 15)
    @Column(name = "NUM_IDENTIFICACION")
    private String identificationNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "NOMBRE_COMPLETO")
    private String fullName;
    @Column(name = "FEC_ULTIMO_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccessDate;
    @Size(max = 64)
    @Column(name = "IP_ULTIMO_INGRESO")
    private String lastAccessIP;
    @Size(max = 6)
    @Column(name = "COD_ISO_IDIOMA")
    private String isoLanguageCode;
    @Size(max = 256)
    @Column(name = "CORREO_ELECTRONICO")
    private String emailAddress;
    @Column(name = "REINTENTOS_FALLIDOS")
    private Integer failedRetries;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "ES_BLOQUEADO_REINTENTOS")
    private SN2Boolean isBlockedByMaxRetries;
    @Column(name = "FEC_BLOQUEO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockingDate;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "ES_ACTIVO")
    private SN2Boolean isActive;
    @JoinTable(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_ROLES_X_USUARIO", joinColumns = {
    		@JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")}, inverseJoinColumns = {
    		@JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL")})
        @ManyToMany
    private List<SegtRole> assignedRoles;
    
    public SegtUser() {}
    
    public SegtUser(Integer userId) {
		super();
		this.userId = userId;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<SegtAccessToken> accessTokens;

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the loginAlias
     */
    public String getLoginAlias() {
        return loginAlias;
    }

    /**
     * @param loginAlias the loginAlias to set
     */
    public void setLoginAlias(String loginAlias) {
        this.loginAlias = loginAlias;
    }

    /**
	 * @return the credentials
	 */
	public SegtUserCredentials getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(SegtUserCredentials credentials) {
		this.credentials = credentials;
	}

    /**
     * @return the identificationNumber
     */
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * @param identificationNumber the identificationNumber to set
     */
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the lastAccessDate
     */
    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    /**
     * @param lastAccessDate the lastAccessDate to set
     */
    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    /**
     * @return the lastAccessIP
     */
    public String getLastAccessIP() {
        return lastAccessIP;
    }

    /**
     * @param lastAccessIP the lastAccessIP to set
     */
    public void setLastAccessIP(String lastAccessIP) {
        this.lastAccessIP = lastAccessIP;
    }

    /**
     * @return the isoLanguageCode
     */
    public String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    /**
     * @param isoLanguageCode the isoLanguageCode to set
     */
    public void setIsoLanguageCode(String isoLanguageCode) {
        this.isoLanguageCode = isoLanguageCode;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the failedRetries
     */
    public Integer getFailedRetries() {
        return failedRetries;
    }

    /**
     * @param failedRetries the failedRetries to set
     */
    public void setFailedRetries(Integer failedRetries) {
        this.failedRetries = failedRetries;
    }

    /**
     * @return the isBlockedByMaxRetries
     */
    public SN2Boolean getIsBlockedByMaxRetries() {
        return isBlockedByMaxRetries;
    }

    /**
     * @param isBlockedByMaxRetries the isBlockedByMaxRetries to set
     */
    public void setIsBlockedByMaxRetries(SN2Boolean isBlockedByMaxRetries) {
        this.isBlockedByMaxRetries = isBlockedByMaxRetries;
    }

    /**
     * @return the blockingDate
     */
    public Date getBlockingDate() {
        return blockingDate;
    }

    /**
     * @param blockingDate the blockingDate to set
     */
    public void setBlockingDate(Date blockingDate) {
        this.blockingDate = blockingDate;
    }

    /**
     * @return the isActive
     */
    public SN2Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(SN2Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the assignedRoles
     */
    public List<SegtRole> getAssignedRoles() {
        return assignedRoles;
    }

    /**
     * @param assignedRoles the assignedRoles to set
     */
    public void setAssignedRoles(List<SegtRole> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }

	/**
	 * @return the accessTokens
	 */
	public List<SegtAccessToken> getAccessTokens() {
		return accessTokens;
	}

	/**
	 * @param accessTokens the accessTokens to set
	 */
	public void setAccessTokens(List<SegtAccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loginAlias == null) ? 0 : loginAlias.hashCode());
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
		if (!(obj instanceof SegtUser))
			return false;
		SegtUser other = (SegtUser) obj;
		if (loginAlias == null) {
			if (other.loginAlias != null)
				return false;
		} else if (!loginAlias.equals(other.loginAlias))
			return false;
		return true;
	}
    
}
