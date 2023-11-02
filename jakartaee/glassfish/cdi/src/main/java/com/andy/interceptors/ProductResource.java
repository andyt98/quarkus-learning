package com.andy.interceptors;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/product-interceptors")
public class ProductResource {

    @Inject
    private ProductService productService;

    @GET
    @Produces("text/plain")
    public String getProduct() {
        return productService.getProduct();
    }
}
