package com.redhat.quarkus.cafe.kitchen.domain;

import com.redhat.quarkus.cafe.kitchen.infrastructure.OrderUpdater;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Kitchen {

    static final Logger logger = LoggerFactory.getLogger(Kitchen.class.getName());

    @Inject
    OrderUpdater orderUpdater;

    public void orderIn(OrderEvent orderIn) {

        logger.debug("Received order: " + orderIn.toString());

        CompletableFuture.runAsync(() -> {

            switch (orderIn.item) {
                case CAKEPOP:
                    prepare(orderIn, 2).thenAccept(o -> orderUpdater.updateOrder(o));
                    break;
                case COOKIE:
                    prepare(orderIn, 2).thenAccept(o -> orderUpdater.updateOrder(o) );
                    break;
                case MUFFIN:
                    prepare(orderIn, 3).thenAccept(o -> orderUpdater.updateOrder(o) );
                    break;
                case PANINI:
                    prepare(orderIn, 10).thenAccept(o -> orderUpdater.updateOrder(o) );
                    break;
                default:
                    prepare(orderIn, 5).thenAccept(o -> orderUpdater.updateOrder(o) );
            }
        });
    }

    private CompletableFuture<OrderEvent> prepare(final OrderEvent orderIn, int seconds) {

        return CompletableFuture.supplyAsync(() -> {

            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            OrderEvent retVal = new OrderEvent(orderIn.orderId, orderIn.name, orderIn.item, orderIn.itemId, EventType.KITCHEN_ORDER_UP);
            return retVal;
        });
    }

}
