package com.redhat.quarkus.cafe.web.infrastructure;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventConverter {

    @Incoming("webui")
    @Outgoing("dashboard")
    @Broadcast
    public String convert(String event) {
        System.out.println("Kafka: " + event);
        return event;
    }

    @Incoming("post")
    @Outgoing("next")
    @Broadcast
    public String id(String x) {
        return x;
    }
}
