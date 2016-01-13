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
    
    
}
