package com.redhat.quarkus.cafe.kitchen.domain;

import com.redhat.quarkus.cafe.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Kitchen {

    static final Logger logger = LoggerFactory.getLogger(Kitchen.class.getName());

    @Inject
    Inventory inventory;

    private String madeBy = "undefined";

    @PostConstruct
    void setHostName() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            if (hostName == null || hostName.length() <= 0) {
                madeBy = "default";
            }
        } catch (IOException e) {
            logger.info("unable to get hostname; using default");
            madeBy = "unknown";
        }
    }

    public CompletableFuture<Event> make(final OrderInEvent orderInEvent) {

        logger.debug("orderIn: " + orderInEvent.toString());
        return CompletableFuture.supplyAsync(() -> {

            switch(orderInEvent.item){
                case CAKEPOP:
                    return prepare(orderInEvent, 5);
                case CROISSANT:
                    return prepare(orderInEvent, 5);
                case CROISSANT_CHOCOLATE:
                    return prepare(orderInEvent, 5);
                case MUFFIN:
                    return prepare(orderInEvent, 7);
                default:
                    return prepare(orderInEvent, 11);
            }
        });
    }

    private Event prepare(final OrderInEvent orderInEvent, int seconds) {

        // decrement the item in inventory
        try {
            inventory.decrementItem(orderInEvent.item);
        } catch (EightySixException e) {
            e.printStackTrace();
            logger.debug(orderInEvent.item + " is 86'd");
            return new EightySixEvent(orderInEvent.item);
        }

        // give the kitchen time to make the item
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new OrderUpEvent(
                EventType.KITCHEN_ORDER_UP,
                orderInEvent.orderId,
                orderInEvent.name,
                orderInEvent.item,
                orderInEvent.itemId,
                madeBy);

    }
}
