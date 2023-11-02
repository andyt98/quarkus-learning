package com.andy;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    private CustomerService customerService;

    @GET
    public Response getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer.get()).build();
    }

    @POST
    public Response createCustomer(@Valid CustomerDTO customerDTO, @Context UriInfo uriInfo) {
        Customer createdCustomer = customerService.create(customerDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdCustomer.getId())).build();
        return Response.created(location).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, @Valid CustomerDTO customerDTO) {
        Customer updatedCustomer = customerService.update(id, customerDTO);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        customerService.delete(id);
        return Response.noContent().build();
    }
}
