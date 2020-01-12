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
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
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

        OrderEvent orderEvent = jsonb.fromJson(message, OrderEvent.class);

        logger.debug(orderEvent);

        System.out.println("order in:" + orderEvent.toString());
        if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
            onKitchenOrderIn(orderEvent);
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
