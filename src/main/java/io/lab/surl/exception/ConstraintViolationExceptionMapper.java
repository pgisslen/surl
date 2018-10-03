package io.lab.surl.exception;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;

public class ConstraintViolationExceptionMapper extends LoggingExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        final int responseCode = HttpStatus.UNPROCESSABLE_ENTITY_422;
        return Response
            .status(responseCode)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorMessage(responseCode, exception.getMessage(), ErrorType.CONSTRAINT_VIOLATION.name()))
            .build();
    }
}
