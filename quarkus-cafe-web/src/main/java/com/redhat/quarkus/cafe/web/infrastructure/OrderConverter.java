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

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.orderEventFromJson;
import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

/**
 * A bean consuming data from the "orders" Kafka topic and applying some conversion.
 * The result is pushed to the "updates" stream which is an in-memory stream.
 */
@ApplicationScoped
public class OrderConverter {

    Logger logger = LoggerFactory.getLogger(OrderConverter.class);

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("barista-in")
    @Outgoing("updates")
    @Broadcast
    public String onBeverageOrderIn(final String payload) {

        logger.debug("Barista event received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);
    }

    @Incoming("kitchen-in")
    @Outgoing("updates")
    @Broadcast
    public String onKitchenOrderIn(final String payload) {

        logger.debug("Kitchen event received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);
    }

    @Incoming("orders-in")
    @Outgoing("updates")
    @Broadcast
    public String onOrderUp(final String payload) {

        logger.debug("OrderUpEvent received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);
    }

    /*
        Returns a properly formatted JSON DashboardUpdate
     */
    private String convertOrderEventToDashboardUpdate(final String payload) {
        final OrderEvent orderEvent = orderEventFromJson(payload);
        return toJson(new DashboardUpdate(orderEvent));
    }


}
