package co.com.binariasystems.orion.business;

import co.com.binariasystems.orion.model.enumerated.SecurityExceptionType;

public class SecurityServicesException extends OrionException{
	private SecurityExceptionType exceptionType;

	public SecurityServicesException(SecurityExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	public SecurityServicesException(SecurityExceptionType exceptionType, String message) {
		super(message);
		this.exceptionType = exceptionType;
	}
	
	public SecurityServicesException(SecurityExceptionType exceptionType, Throwable cause) {
		super(cause.getMessage(), cause);
		this.exceptionType = exceptionType;
	}

	public SecurityServicesException() {
		super();
	}

	public SecurityServicesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SecurityServicesException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SecurityServicesException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SecurityServicesException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the exceptionType
	 */
	public SecurityExceptionType getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(SecurityExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	
}
