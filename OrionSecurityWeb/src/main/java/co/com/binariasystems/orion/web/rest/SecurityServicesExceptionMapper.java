package co.com.binariasystems.orion.web.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import co.com.binariasystems.orion.business.SecurityServicesException;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

@Provider
public class SecurityServicesExceptionMapper implements ExceptionMapper<SecurityServicesException>{

	@Override
	public Response toResponse(SecurityServicesException ex) {
		ResponseBuilder responseBuilder = Response.serverError()
				.header(OrionWebConstants.EXCEPTION_MSG_HEADER_NAME, ex.getMessage());
		if(ex.getExceptionType() != null)
			responseBuilder.header(OrionWebConstants.EXCEPTION_TYPE_HEADER_NAME, ex.getExceptionType().name());
		return responseBuilder.build();
	}

}
