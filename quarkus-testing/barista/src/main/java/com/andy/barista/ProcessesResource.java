package com.andy.barista;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("processes")
public class ProcessesResource {

    @Inject
    OrderStatusProcessor processor;

    @POST
    public JsonObject process(JsonObject order) {
        final String status = order.getString("status", null);
        String newStatus = processor.process(status);
        return Json.createObjectBuilder().add("status", newStatus).build();
    }

}
