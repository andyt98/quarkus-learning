package com.redhat.training;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Path("/speakers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SpeakerResource {

    private final SpeakerIdGenerator generator = new SpeakerIdGenerator();

    @GET
    @RolesAllowed("read")
    public List<Speaker> getSpeakers() {
        return Speaker.listAll();
    }

    @GET
    @Path("/{uuid}")
    @RolesAllowed("read")
    public Optional<Speaker> findByUuid(@PathParam("uuid") String uuid) {

        if (uuid == null) {
            throw new NotFoundException();
        }
        return Speaker.find("uuid", uuid).firstResultOptional();
    }

    @Transactional
    @POST
    @RolesAllowed("modify")
    public Speaker insert(Speaker speaker) {
        speaker.uuid=generator.generate();
        speaker.persist();
        return speaker;
    }

    private static class SpeakerIdGenerator {

        public String generate() {
            return UUID.randomUUID().toString();
        }
    }

    @Transactional
    @PUT
    @Path("/{uuid}")
    @RolesAllowed("modify")
    public Speaker update(@PathParam("uuid") String uuid, Speaker speaker) {
        if (null != uuid) {
            Speaker.find("uuid", uuid);
        }
        throw new NotFoundException();

    }
}
