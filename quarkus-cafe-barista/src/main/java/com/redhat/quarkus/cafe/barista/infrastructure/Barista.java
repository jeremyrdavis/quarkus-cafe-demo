package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.OrderEvent;
import com.redhat.quarkus.cafe.barista.domain.EventType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Barista {

    Logger logger = LoggerFactory.getLogger(Barista.class);

    @Inject @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> orderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());

        final OrderEvent order = jsonb.fromJson((String) message.getPayload(), OrderEvent.class);

        if (order.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {

            orderIn(order).toCompletableFuture();
        }

        return message.ack();
    }

    public CompletionStage<Void> orderIn(OrderEvent order) {

        logger.debug("orderIn: " + order.toString());

        return CompletableFuture.supplyAsync(() -> {

            switch(order.item){
                case COFFEE_BLACK:
                    return prepare(order, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(order, 5);
                case ESPRESSO:
                    return prepare(order, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(order, 7);
                case LATTE:
                    return prepare(order, 7);
                case CAPPUCCINO:
                    return prepare(order, 9);
                default:
                    return prepare(order, 11);
            }
        }).thenAccept(b -> {
            logger.debug("returning: {}", b);
            orderUpEmitter.send(jsonb.toJson(b));
        });
    }

    private OrderEvent prepare(final OrderEvent order, int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        OrderEvent retVal = new OrderEvent(EventType.BEVERAGE_ORDER_UP, order.orderId, order.itemId, order.name, order.item);
        System.out.println("returning: " + retVal.toString());
        return retVal;
    }

}
