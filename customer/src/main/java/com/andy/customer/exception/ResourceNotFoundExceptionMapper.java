package com.andy.customer.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(
                uriInfo.getPath(), // Get the path from the UriInfo
                ex.getMessage(),
                404,
                LocalDateTime.now()
        );
        return Response.status(Response.Status.NOT_FOUND).entity(apiError).build();
    }
}
