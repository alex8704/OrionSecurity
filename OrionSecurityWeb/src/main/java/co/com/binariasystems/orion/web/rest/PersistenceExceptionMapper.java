package co.com.binariasystems.orion.web.rest;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import co.com.binariasystems.orion.web.utils.OrionWebConstants;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
	@Override
	public Response toResponse(PersistenceException ex) {
		return Response.serverError()
				.header(OrionWebConstants.EXCEPTION_MSG_HEADER_NAME, ex.getMessage()).build();
	}

}
