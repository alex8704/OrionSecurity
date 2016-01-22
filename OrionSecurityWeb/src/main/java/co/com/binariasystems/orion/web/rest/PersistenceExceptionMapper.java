package co.com.binariasystems.orion.web.rest;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.binariasystems.orion.web.utils.OrionWebConstants;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceExceptionMapper.class);
	@Override
	public Response toResponse(PersistenceException ex) {
		LOGGER.error("Mapping exception", ex);
		return Response.serverError()
				.header(OrionWebConstants.EXCEPTION_MSG_HEADER_NAME, ex.getMessage()).build();
	}

}
