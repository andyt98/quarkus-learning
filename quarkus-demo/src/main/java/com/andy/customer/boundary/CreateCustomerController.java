package com.andy.customer.boundary;

import com.andy.customer.control.CustomerService;
import com.andy.customer.entity.CustomerCreateRequest;
import com.andy.customer.entity.CustomerType;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Set;


@ApplicationScoped
@Path("create.html")
@Produces(MediaType.TEXT_HTML)
@Blocking
//@RolesAllowed("admin")
public class CreateCustomerController {

    @Location("create.html")
    Template template;

    @Inject
    CustomerService customerService;

    @Inject
    Validator validator;


    @GET
    public TemplateInstance form() {
        return template.data("types", CustomerType.values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("type") CustomerType type,
                           @FormParam("username") String username,
                           @FormParam("email") String email) {

        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(username, email, type);

        Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(customerCreateRequest);

        if (!violations.isEmpty()) {
            // Handle validation errors, possibly with more detailed messages
            StringBuilder errorMessage = new StringBuilder("Validation errors:");
            for (ConstraintViolation<CustomerCreateRequest> violation : violations) {
                errorMessage.append("\n- ").append(violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage.toString())
                    .build();
        }

        try {
            customerService.create(customerCreateRequest);
            return Response.seeOther(URI.create("/customers.html")).build();
        } catch (Exception e) {
            // Handle any exceptions (e.g., database errors)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create customer.")
                    .build();
        }
    }


}
