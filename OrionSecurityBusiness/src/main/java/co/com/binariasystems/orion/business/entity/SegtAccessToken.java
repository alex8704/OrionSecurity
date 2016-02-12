package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;
import co.com.binariasystems.orion.business.utils.OrionBusinessConstants;

@Entity
@Table(schema=OrionBusinessConstants.ORION_DBSCHEMA, name = "SEGT_TOKENS_ACCESO")
@NamedQueries({
    @NamedQuery(name = "SegtAccessToken.findAll", query = "SELECT t FROM SegtAccessToken t")})
public class SegtAccessToken implements Serializable {
	@EmbeddedId
	private SegtAccessTokenPK id;
	@Size(max = 64)
    @Column(name = "TOKEN")
    private String tokenString;
	@NotNull
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
    @Column(name = "FEC_EXPIRACION")
    @Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "ES_VIGENTE")
	private SN2Boolean isActive;
	
	@JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private SegtUser user;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private SegtApplication application;
    
    public SegtAccessToken(){}
    
	/**
	 * @return the id
	 */
	public SegtAccessTokenPK getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(SegtAccessTokenPK id) {
		this.id = id;
	}
	/**
	 * @return the tokenString
	 */
	public String getTokenString() {
		return tokenString;
	}
	/**
	 * @param tokenString the tokenString to set
	 */
	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
	 * @return the user
	 */
	public SegtUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(SegtUser user) {
		this.user = user;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof SegtAccessToken))
			return false;
		SegtAccessToken other = (SegtAccessToken) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    
}
