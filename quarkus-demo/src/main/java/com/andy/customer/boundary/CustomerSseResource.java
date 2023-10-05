package com.andy.customer.boundary;


import com.andy.customer.control.Created;
import com.andy.customer.control.CustomerDtoMapper;
import com.andy.customer.control.CustomerMapper;
import com.andy.customer.entity.Customer;
import com.andy.customer.entity.CustomerDto;
import io.smallrye.mutiny.Multi;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import org.eclipse.microprofile.reactive.messaging.Channel;

import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
@Path("customers/sse")
public class CustomerSseResource {

    @Inject
    @Channel("customer-updates")
    Multi<CustomerDto> customersUpdates;

    @Inject
    CustomerDtoMapper customerDtoMapper;

    @Inject
    CustomerMapper customerMapper;

    @Context
    Sse sse;

    private SseBroadcaster broadcaster;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<JsonObject> customers() {
        return Multi.createBy().merging()
                .streams(customersUpdates.map(customerDtoMapper::toJson), ping());
    }

    private Multi<JsonObject> ping() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(10))
                .onItem().transform(l -> Json.createObjectBuilder().build());
    }

    @GET
    @Path("/alternative")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void customersAlternative(@Context SseEventSink eventSink) {
        if (sse != null) {
            broadcaster.register(eventSink);
        }
    }


    public void onCreatedCustomer(@ObservesAsync @Created Customer customer) {
        if (broadcaster != null) {
            OutboundSseEvent event = sse.newEventBuilder()
                    .id(customer.getUuid().toString() + "-" + Instant.now().getEpochSecond())
                    .name("created")
                    .data(customerMapper.toDTO(customer).toString()).build();
            broadcaster.broadcast(event);
        }
    }


    @PostConstruct
    void init() {
        broadcaster = sse.newBroadcaster();
    }

    @PreDestroy
    void close() {
        broadcaster.close();
    }

}
