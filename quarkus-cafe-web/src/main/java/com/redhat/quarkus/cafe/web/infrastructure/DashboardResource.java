package com.redhat.quarkus.cafe.web.infrastructure;

import io.smallrye.reactive.messaging.annotations.Channel;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/dashboard")
public class DashboardResource {

    @Inject
    @Channel("updates")
    PublisherBuilder<String> updater;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
    public Publisher<String> dashboardStream() {
        return updater.peek(order -> {
            System.out.println(order);
        }).buildRs();
    }
}
