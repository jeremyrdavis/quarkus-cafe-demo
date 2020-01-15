package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;

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

    @ConsumeEvent("next")
    public DashboardUpdate dashboardUpdate(DashboardUpdate dashboardUpdate){ return dashboardUpdate; };

}
