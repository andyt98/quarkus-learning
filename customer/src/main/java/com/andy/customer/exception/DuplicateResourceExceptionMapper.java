package com.andy.customer.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(DuplicateResourceException ex) {
        ApiError apiError = new ApiError(
                uriInfo.getPath(), // Get the path from the UriInfo
                ex.getMessage(),
                409,
                LocalDateTime.now()
        );
        return Response.status(Response.Status.CONFLICT).entity(apiError).build();
    }
}
