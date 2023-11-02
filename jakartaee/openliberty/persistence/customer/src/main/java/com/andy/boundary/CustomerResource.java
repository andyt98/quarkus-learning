package com.andy.boundary;

import com.andy.control.CustomerDTO;
import com.andy.control.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/customers")
public class CustomerResource {

    @Inject
    private CustomerService customerService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(@Valid CustomerCreateRequest createCustomerRequest) {
        customerService.create(createCustomerRequest);
        Long createdCustomerId = customerService.findByEmail(createCustomerRequest.getEmail()).getId();
        URI location = uriInfo.getAbsolutePathBuilder().path(createdCustomerId.toString()).build();
        return Response.created(location).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerDTO> getAllCustomers() {
        return customerService.findAll();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public CustomerDTO getCustomerById(@PathParam("id") Long id) {
        return customerService.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") Long id, @Valid CustomerUpdateRequest updateRequest) {
        customerService.update(id, updateRequest);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") Long id) {
        customerService.delete(id);
        return Response.noContent().build();
    }
}
