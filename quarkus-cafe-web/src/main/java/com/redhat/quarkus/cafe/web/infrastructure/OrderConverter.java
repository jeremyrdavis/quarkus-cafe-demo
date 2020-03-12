package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import com.redhat.quarkus.cafe.web.domain.OrderEvent;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "updates" stream which is an in-memory stream.
 */
@ApplicationScoped
public class OrderConverter {

    Logger logger = LoggerFactory.getLogger(OrderConverter.class);

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-incoming")
    @Outgoing("updates")
    @Broadcast
    public String process(String payload) {

        OrderEvent incomingEvent = jsonb.fromJson(payload, OrderEvent.class);
        logger.debug("Event received {}", incomingEvent);
        DashboardUpdate dashboardUpdate = new DashboardUpdate(incomingEvent);
        logger.debug(dashboardUpdate.toString());
        return jsonb.toJson(dashboardUpdate).toString();
    }

}
