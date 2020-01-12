package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.KitchenOrder;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.StringReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class KafkaResource {

    Logger logger = Logger.getLogger(KafkaResource.class);

    @Inject
    Kitchen kitchen;

    Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orderin")
    public void orderIn(String message) {

        System.out.println("\nmessage received:\n" + message);
        logger.debug("\nOrder Received:\n" + message);

        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        if (eventType.equals(EventType.KITCHEN_ORDER_IN.toString())) {

            logger.debug("\nKitchen Order In Received:\n");

            OrderEvent orderEvent = jsonb.fromJson(message, OrderEvent.class);

            logger.debug("\nKitchen Order In Received:\n" + orderEvent.toString());

            System.out.println("order in:" + orderEvent.toString());
            if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                onKitchenOrderIn(orderEvent);
            }
        }

    }

//    @Outgoing("kitchen-orders-up")
    private void onKitchenOrderIn(final OrderEvent orderEvent) {

        OrderEvent orderUp = null;
        try {
            orderUp = kitchen.orderIn(orderEvent).get();
            System.out.println(orderUp.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
