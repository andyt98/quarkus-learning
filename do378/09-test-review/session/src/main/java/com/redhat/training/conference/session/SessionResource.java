package com.redhat.training.conference.session;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;


@Path("sessions")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource {

    @Inject
    SessionStore sessionStore;

    @GET
    public Collection<SessionWithSpeaker> getAllSessions() throws Exception {
        return sessionStore.getAll();
    }

    @GET
    @Path("/{sessionId}")
    public SessionWithSpeaker getSession(@PathParam("sessionId") final Long sessionId) {
        return sessionStore.getById(sessionId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Session createSession(final Session session) {
        return sessionStore.save(session);
    }
}
