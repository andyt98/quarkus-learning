package com.redhat.training.conference.speaker;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;


@Path("/speakers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public interface SpeakerServiceClient {

    @GET
    List<Speaker> getSpeakers();

    @GET
    @Path("/{id}")
    Speaker getSpeaker(@PathParam("id") Long id);
}
