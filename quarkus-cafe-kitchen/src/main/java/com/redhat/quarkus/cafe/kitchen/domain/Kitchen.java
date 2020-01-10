package com.redhat.quarkus.cafe.kitchen.domain;

import com.redhat.quarkus.cafe.kitchen.infrastructure.EventUpdateService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class Kitchen {

    static final Logger LOGGER = Logger.getLogger(Kitchen.class.getName());

    @Inject
    EventUpdateService eventUpdateService;

    public CompletionStage<KitchenOrder> orderIn(KitchenOrder kitchenOrder) {

        LOGGER.info("Received order: " + kitchenOrder.toString());
        LOGGER.info("Sending order at " + Instant.now().toString() + " " + kitchenOrder.toString());

        return CompletableFuture.supplyAsync(() -> {

            switch (kitchenOrder.getMenuItem()) {
                case COOKIE:
                    return prepare(kitchenOrder, 2);
                case MUFFIN:
                    return prepare(kitchenOrder, 3);
                case PANINI:
                    return prepare(kitchenOrder, 10);
                default:
                    return prepare(kitchenOrder, 5);
            }
        });
    }

    private KitchenOrder prepare(final KitchenOrder kitchenOrderIn, int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        KitchenOrder retVal = new KitchenOrder(kitchenOrderIn.orderNumber, kitchenOrderIn.name, kitchenOrderIn.menuItem, OrderStatus.READY);
        System.out.println("returning: " + retVal.toString());
        return retVal;
    }

    private void prepare(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
