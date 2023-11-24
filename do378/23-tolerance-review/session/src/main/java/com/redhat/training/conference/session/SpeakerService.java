package com.redhat.training.conference.session;

import java.util.Collection;
import java.util.List;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/speaker")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public interface SpeakerService {
    @GET
    List<SpeakerFromService> listAll();

    @GET
    @Path("/{id}")
    Speaker getById(@PathParam("id") int id);

    @GET
    @Path("/search")
    Collection<SpeakerFromService> search(@QueryParam("query") String query, @QueryParam("sort") String sort);
}
