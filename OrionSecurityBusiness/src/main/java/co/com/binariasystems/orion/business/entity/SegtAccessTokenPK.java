package co.com.binariasystems.orion.business.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class SegtAccessTokenPK implements Serializable {
	@Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_APLICACION")
    private int applicationId;
    
    public SegtAccessTokenPK(){}
    
	public SegtAccessTokenPK(int userId, int applicationId) {
		super();
		this.userId = userId;
		this.applicationId = applicationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + applicationId;
		result = prime * result + userId;
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
		if (!(obj instanceof SegtAccessTokenPK))
			return false;
		SegtAccessTokenPK other = (SegtAccessTokenPK) obj;
		if (applicationId != other.applicationId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
    
}
