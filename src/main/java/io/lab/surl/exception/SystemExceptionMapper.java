package io.lab.surl.exception;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CatchAllExceptionMapper extends LoggingExceptionMapper<SystemException> {

    @Override
    public Response toResponse(SystemException exception) {
        final int responseCode = exception.getErrorType().getResponseCode();
        return Response
            .status(responseCode)
            .type(MediaType.APPLICATION_XML_TYPE)
            .entity(new ErrorMessage(responseCode, exception.getMessage(),exception.getErrorType().name()))
            .build();
    }
}
