package com.andy.customer.control;

import com.andy.customer.boundary.CustomerResource;
import com.andy.customer.entity.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@ApplicationScoped
public class CustomerDtoMapper {

    @Context
    UriInfo uriInfo;

    public JsonObject toJson(CustomerDto customerDto) {

        // Construct the customer URI by appending the relative path to the base URI
        URI customerUri = UriBuilder.fromUri(uriInfo.getBaseUri())
                .path(CustomerResource.class)
                .path(customerDto.getUuid().toString()).build();

        return Json.createObjectBuilder()
                .add("uuid", customerDto.getUuid().toString())
                .add("name", customerDto.getName())
                .add("email", customerDto.getEmail())
                .add("type", customerDto.getCustomerType().toString())
                .add("_links", Json.createObjectBuilder()
                        .add("self", customerUri.toString()))
                .build();
    }


}
