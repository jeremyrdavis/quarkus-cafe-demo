package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.StringReader;

public class KafkaResource {

    private static final Logger logger = LoggerFactory.getLogger(KafkaResource.class);

    @Inject
    Kitchen kitchen;

    final Jsonb jsonb = JsonbBuilder.create();

/*
    @Incoming("orders-in")
    public void orderIn(String message) {

        logger.debug("message received: {}", message);

        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        if (eventType.equals(EventType.KITCHEN_ORDER_IN.toString())) {

            OrderEvent orderEvent = jsonb.fromJson(message, OrderEvent.class);
            logger.debug("kitchen order in: {}", orderEvent);
            kitchen.orderIn(orderEvent);
        }

    }
*/
}
