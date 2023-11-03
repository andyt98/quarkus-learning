package com.redhat.training;


import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/suggestion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SuggestionResource {

    @POST
    public Uni<Suggestion> create(Suggestion newSuggestion) {
        return Panache.withTransaction(newSuggestion::persist);
    }

    @GET
    @Path("/{id}")
    public Uni<Suggestion> get(Long id) {
        return Suggestion.findById(id);
    }

    @GET
    public Uni<List<Suggestion>> list() {
        return Suggestion.listAll();
    }

    @DELETE
    public Uni<Long> deleteAll() {
        return Suggestion.deleteAll();
    }
}
