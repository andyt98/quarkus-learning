package com.andy.customer.boundary;

import com.andy.customer.control.CustomerService;
import com.andy.customer.entity.CustomerCreateRequest;
import com.andy.customer.entity.CustomerDto;
import com.andy.customer.entity.CustomerType;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @Context
    UriInfo uriInfo;

    @PostConstruct
    private void init(){
        customerService.create(new CustomerCreateRequest("customer1", "customer1@test.com", CustomerType.REGULAR));
        customerService.create(new CustomerCreateRequest("customer2", "customer2@test.com", CustomerType.VIP));
    }

    @POST
    public Response createCustomer(@Valid CustomerCreateRequest createCustomerRequest) {
        customerService.create(createCustomerRequest);
        UUID createdCustomerId = customerService.findByEmail(createCustomerRequest.getEmail()).getUuid();
        URI location = uriInfo.getAbsolutePathBuilder().path(createdCustomerId.toString()).build();
        return Response.created(location).build();
    }

    @GET
    public List<CustomerDto> getAllCustomers() {
        return customerService.findAll();
    }

    @GET
    @Path("/alternative")
    public JsonArray getAllCustomersAlternative()  {
        return customerService.findAll().stream()
                .map(this::mapToJson)
                .collect(JsonCollectors.toJsonArray());
    }

    private JsonObject mapToJson(CustomerDto customerDto) {
        // Get the base URI from UriInfo
        URI baseUri = uriInfo.getBaseUri();

        // Construct the customer URI by appending the relative path to the base URI
        URI customerUri = UriBuilder.fromUri(baseUri).path(CustomerResource.class).path(customerDto.getUuid().toString()).build();

        return Json.createObjectBuilder()
                .add("uuid", customerDto.getUuid().toString())
                .add("name", customerDto.getName())
                .add("email", customerDto.getEmail())
                .add("type", customerDto.getCustomerType().toString())
                .add("_links", Json.createObjectBuilder()
                        .add("self", customerUri.toString()))  // Use the constructed URI
                .build();
    }


    @GET
    @Path("/{id}")
    public CustomerDto getCustomerById(@PathParam("id") UUID id) {
        return customerService.findById(id);
    }


    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") UUID id) {
        customerService.delete(id);
        return Response.noContent().build();
    }
}
