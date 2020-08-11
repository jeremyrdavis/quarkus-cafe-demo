package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletionStage;

public class KafkaResource {

    private static final Logger logger = LoggerFactory.getLogger(KafkaResource.class);

    @Inject
    Kitchen kitchen;

    @Inject @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    final Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> handleOrderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());
        final OrderInEvent orderIn = jsonb.fromJson((String) message.getPayload(), OrderInEvent.class);
        if (orderIn.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
            return kitchen.make(orderIn).thenApply(o -> {
                return orderUpEmitter.send(jsonb.toJson(o));
            }).thenRun( () -> { message.ack(); });
        }else{
            return message.ack();
        }
    }
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
