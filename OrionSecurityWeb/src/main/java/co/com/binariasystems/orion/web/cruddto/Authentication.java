package co.com.binariasystems.orion.web.cruddto;

import java.io.Serializable;

public class Authentication implements Serializable {
	private String username;
	private String password;
	private Boolean rememberMe;
	private String netAddress;
	private String applicationCode;
	
	public Authentication(){}
	
	public Authentication(String username, String password, Boolean rememberMe, String applicationCode) {
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.applicationCode = applicationCode;
	}

	public Authentication(String username, String password, String applicationCode) {
		super();
		this.username = username;
		this.password = password;
		this.applicationCode = applicationCode;
	}
	
	public Authentication(String username, String applicationCode) {
		this.username = username;
		this.applicationCode = applicationCode;
	}

	public Authentication(String username, String password, Boolean rememberMe) {
		super();
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
	}
	
	public Authentication(String username, String password, Boolean rememberMe, String applicationCode, String netAddress) {
		super();
		this.username = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.applicationCode = applicationCode;
		this.netAddress = netAddress;
	}
	
	public Authentication(String username, String password, String applicationCode, String netAddress) {
		super();
		this.username = username;
		this.password = password;
		this.applicationCode = applicationCode;
		this.netAddress = netAddress;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	/**
	 * @return the applicationCode
	 */
	public String getApplicationCode() {
		return applicationCode;
	}

	/**
	 * @param applicationCode the applicationCode to set
	 */
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getNetAddress() {
		return netAddress;
	}

	public void setNetAddress(String netAddress) {
		this.netAddress = netAddress;
	}
	
	
}
