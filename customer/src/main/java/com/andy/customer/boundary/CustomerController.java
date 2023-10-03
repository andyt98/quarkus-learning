package com.andy.customer.boundary;

import com.andy.customer.control.CustomerService;
import com.andy.customer.entity.CustomerDto;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Path("/customers.html")
@Produces(MediaType.TEXT_HTML)
public class CustomerController {

    @Location("customers.html")
    Template customersTemplate;

    @Inject
    CustomerService customerService;

    @GET
    @Blocking
    public TemplateInstance customers(){
        List<CustomerDto> customers = customerService.findAll();
        return customersTemplate.data("customers", customers);
    }

    @TemplateExtension
    static String format(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).withNano(0)
                .format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
