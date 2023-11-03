package com.redhat.training.conference.speaker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/speaker")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
@ApplicationScoped
public interface SpeakerService {
    @GET
    List<Speaker> listAll();

    @GET
    @Path("/{id}")
    Speaker getById(@PathParam("id") int id);
}
