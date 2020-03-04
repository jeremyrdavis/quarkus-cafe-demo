package com.redhat.quarkus.cafe.web.infrastructure;

import io.smallrye.reactive.messaging.annotations.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/dashboard")
public class DashboardResource {

    Logger logger = LoggerFactory.getLogger(DashboardResource.class);

    @Inject @Channel("dashboard")
    Publisher<String> updates;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<String> stream() {

        try {
            return updates;
        } catch (Exception e) {
            logger.error("SSE Error {}", e.getMessage());
        }
        return null;
    }

}
