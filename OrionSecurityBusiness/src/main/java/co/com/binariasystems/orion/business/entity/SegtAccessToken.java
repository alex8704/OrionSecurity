package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;
import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.com.binariasystems.orion.business.enumerated.SN2Boolean;

@Entity
@Table(name = "SEGT_TOKENS_ACCESO")
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
	private Timestamp creationDate;
    @Column(name = "FEC_EXPIRACION")
    @Temporal(TemporalType.TIMESTAMP)
	private Timestamp expirationDate;
	@Size(max = 1)
    @Enumerated(EnumType.STRING)
    @Column(name = "ES_VIGENTE")
	private SN2Boolean isActive;
	
	@JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SegtUser user;
    @JoinColumn(name = "ID_APLICACION", referencedColumnName = "ID_APLICACION", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SegtApplication application;
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
	public Timestamp getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the expirationDate
	 */
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Timestamp expirationDate) {
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
    
    
}
