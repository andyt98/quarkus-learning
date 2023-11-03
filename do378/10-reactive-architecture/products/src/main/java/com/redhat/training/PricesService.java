package com.redhat.training;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;


@RegisterRestClient(baseUri = "http://localhost:5000")
@Path("/prices")
@Produces(MediaType.APPLICATION_JSON)
public interface PricesService {

    @GET
    @Path("/history/{productId}")
    Uni<ProductPriceHistory> getProductPriceHistory(@PathParam("productId") final Long productId);
}
