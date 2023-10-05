package com.andy.customer.boundary;

import com.andy.customer.control.CustomerDtoMapper;
import com.andy.customer.control.CustomerService;
import com.andy.customer.entity.CustomerCreateRequest;
import com.andy.customer.entity.CustomerDto;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
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

    @Inject
    CustomerDtoMapper customerDtoMapper;


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
    public JsonArray getAllCustomersAlternative() {
        return customerService.findAll().stream()
                .map(customerDtoMapper::toJson)
                .collect(JsonCollectors.toJsonArray());
    }


    @GET
    @Path("/{uuid}")
    public CustomerDto getCustomerById(@PathParam("uuid") UUID uui) {
        return customerService.findById(uui);
    }


    @DELETE
    @Path("/{uuid}")
    public Response deleteCustomer(@PathParam("uuid") UUID uuid) {
        customerService.delete(uuid);
        return Response.noContent().build();
    }
}