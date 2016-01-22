package co.com.binariasystems.orion.web.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import co.com.binariasystems.fmw.util.exception.FMWExceptionUtils;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

@Provider
public class DefaultExceptionMappper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable throwable) {
		Throwable cause = FMWExceptionUtils.prettyMessageException(throwable);
		return Response.serverError()
				.header(OrionWebConstants.EXCEPTION_MSG_HEADER_NAME, cause.getMessage()).build();
	}
}
