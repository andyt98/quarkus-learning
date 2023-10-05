package com.andy.customer.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class RequestValidationExceptionMapper implements ExceptionMapper<RequestValidationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(RequestValidationException ex) {
        ApiError apiError = new ApiError(
                uriInfo.getPath(), // Get the path from the UriInfo
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
    }
}
