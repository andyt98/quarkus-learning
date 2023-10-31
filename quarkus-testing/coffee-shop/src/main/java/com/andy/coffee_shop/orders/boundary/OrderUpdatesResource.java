package com.andy.coffee_shop.orders.boundary;

import com.andy.coffee_shop.orders.entity.Order;
import io.quarkus.scheduler.Scheduled;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@ApplicationScoped
@Path("orders/updates")
public class OrderUpdatesResource {

    @Context
    Sse sse;

    private SseBroadcaster broadcaster;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void orders(@Context SseEventSink eventSink) {
        if (broadcaster == null)
            broadcaster = sse.newBroadcaster();
        if (sse != null)
            broadcaster.register(eventSink);
    }

    public void onUpdatedOrder(@Observes(during = TransactionPhase.AFTER_COMPLETION) Order order) {
        OutboundSseEvent event = sse.newEventBuilder().name("update")
                .data("/orders/" + order.getId()).build();
        broadcaster.broadcast(event);
    }

    @Scheduled(every = "10s")
    public void onPing() {
        if (broadcaster != null) {
            OutboundSseEvent event = sse.newEventBuilder().name("ping").data("").build();
            broadcaster.broadcast(event);
        }
    }

    @PreDestroy
    void close() {
        if (broadcaster != null)
            broadcaster.close();
    }

}
