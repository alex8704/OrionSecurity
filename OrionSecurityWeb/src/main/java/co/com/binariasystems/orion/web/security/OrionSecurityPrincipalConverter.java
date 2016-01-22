package co.com.binariasystems.orion.web.security;

import co.com.binariasystems.fmw.security.FMWSecurityException;
import co.com.binariasystems.fmw.security.authc.SecurityPrincipalConverter;

public class OrionSecurityPrincipalConverter implements SecurityPrincipalConverter<String, Object> {
	
	public OrionSecurityPrincipalConverter(){
		
	}
	
	@Override
	public Object toPrincipalModel(String representation) throws FMWSecurityException {
		return representation;
	}

	@Override
	public String toPrincipalRepresentation(Object model) throws FMWSecurityException {
		return model.toString();
	}
}
