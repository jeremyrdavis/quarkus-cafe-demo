package com.redhat.quarkus.cafe.web.infrastructure;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class EventConverter {

    Jsonb jsonb = JsonbBuilder.create();

    @Incoming("updates")
    @Outgoing("dashboard")
    @Broadcast
    public String process(String event) {
        return event;
    }
}
