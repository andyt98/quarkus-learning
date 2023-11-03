package com.redhat.training;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("/prices")
@ApplicationScoped
public class PriceResource {

    private List<ProductPriceHistory> productPriceHistoryList;

    @PostConstruct
    public void init() {
        this.productPriceHistoryList = List.of(
                new ProductPriceHistory(1L, getPriceHistoryForProduct()),
                new ProductPriceHistory(2L, getPriceHistoryForProduct()),
                new ProductPriceHistory(3L, getPriceHistoryForProduct())
        );
    }

    private List<Price> getPriceHistoryForProduct() {
        return List.of(
                new Price(new Date(), 19.99),
                new Price(new Date(), 29.99),
                new Price(new Date(), 39.99)
        );
    }


    @GET
    @Path("/history/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductPriceHistory(@PathParam("productId") Long productId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        ProductPriceHistory priceHistory = productPriceHistoryList.stream()
                .filter(productPriceHistory -> productPriceHistory.getProductId().equals(productId))
                .findFirst()
                .orElseThrow();
        return Response.ok(priceHistory).build();
    }


}
