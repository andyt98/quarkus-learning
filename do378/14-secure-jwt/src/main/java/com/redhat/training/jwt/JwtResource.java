package com.redhat.training.jwt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static com.redhat.training.jwt.JwtGenerator.generateJwtForAdmin;
import static com.redhat.training.jwt.JwtGenerator.generateJwtForRegularUser;


@Path("/jwt")
@ApplicationScoped
public class JwtResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}")
    public String getJwt(@PathParam("username") String username) {

        if (username.equalsIgnoreCase("admin")) {
            return generateJwtForAdmin(username);
        }

        return generateJwtForRegularUser(username);
    }

}
