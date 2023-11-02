package com.andy.specializations;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/product")
@RequestScoped
public class ProductResource {

    @Inject
    private Product product;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getProductInfo() {
        return "Product Name: " + product.getName();
    }
}
