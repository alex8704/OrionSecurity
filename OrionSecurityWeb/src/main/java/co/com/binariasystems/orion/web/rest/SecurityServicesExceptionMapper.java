package co.com.binariasystems.orion.web.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.orion.business.SecurityServicesException;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

@Provider
public class SecurityServicesExceptionMapper implements ExceptionMapper<SecurityServicesException>{
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServicesExceptionMapper.class);

	@Override
	public Response toResponse(SecurityServicesException ex) {
		LOGGER.error("Mapping exception", ex);
		ResponseBuilder responseBuilder = Response.serverError()
				.header(OrionWebConstants.EXCEPTION_MSG_HEADER_NAME, ex.getMessage());
		if(ex.getExceptionType() != null)
			responseBuilder.header(OrionWebConstants.EXCEPTION_TYPE_HEADER_NAME, ex.getExceptionType().name());
		return responseBuilder.build();
	}

}
