package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;
import java.util.Date;

import co.com.binariasystems.orion.model.enumerated.SN2Boolean;

public class User implements Serializable {
	private Integer userId;
    private String loginAlias;
    private UserCredentials credentials;
    private String identificationTypeCode;
    private String identificationNumber;
    private String fullName;
    private Date lastAccessDate;
    private String lastAccessIP;
    private String isoLanguageCode;
    private String emailAddress;
    private Integer failedRetries;
    private SN2Boolean isBlockedByMaxRetries;
    private Date blockingDate;
    private SN2Boolean isActive;
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
	public UserCredentials getCredentials() {
		return credentials;
	}
	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(UserCredentials credentials) {
		this.credentials = credentials;
	}
	/**
	 * @return the identificationTypeCode
	 */
	public String getIdentificationTypeCode() {
		return identificationTypeCode;
	}
	/**
	 * @param identificationTypeCode the identificationTypeCode to set
	 */
	public void setIdentificationTypeCode(String identificationTypeCode) {
		this.identificationTypeCode = identificationTypeCode;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((identificationNumber == null) ? 0 : identificationNumber.hashCode());
		result = prime * result + ((identificationTypeCode == null) ? 0 : identificationTypeCode.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (credentials == null) {
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (identificationNumber == null) {
			if (other.identificationNumber != null)
				return false;
		} else if (!identificationNumber.equals(other.identificationNumber))
			return false;
		if (identificationTypeCode == null) {
			if (other.identificationTypeCode != null)
				return false;
		} else if (!identificationTypeCode.equals(other.identificationTypeCode))
			return false;
		if (loginAlias == null) {
			if (other.loginAlias != null)
				return false;
		} else if (!loginAlias.equals(other.loginAlias))
			return false;
		return true;
	}
    
}
