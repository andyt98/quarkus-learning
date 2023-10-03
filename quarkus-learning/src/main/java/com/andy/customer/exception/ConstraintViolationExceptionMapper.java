package com.andy.customer.exception;


import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
        e.getConstraintViolations().forEach(v -> builder.header("X-Validation-Error", v.getPropertyPath() + " " + v.getMessage()));
        return builder.build();
    }

}
