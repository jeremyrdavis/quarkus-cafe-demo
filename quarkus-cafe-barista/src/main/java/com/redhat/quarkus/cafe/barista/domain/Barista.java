package com.redhat.quarkus.cafe.barista.domain;

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
public class Barista {

    static final Logger logger = LoggerFactory.getLogger(Barista.class);

    private String madeBy;

    @Inject
    Inventory inventory;

    @PostConstruct
    void setHostName() {
        try {
            madeBy = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            madeBy = "unknown";
        }
    }

    public CompletableFuture<Collection<Event>> make(final OrderInEvent orderInEvent) {

        logger.debug("making: {}" + orderInEvent.item);

        return CompletableFuture.supplyAsync(() -> {

            switch (orderInEvent.item) {
                case COFFEE_BLACK:
                    return prepare(orderInEvent, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(orderInEvent, 5);
                case ESPRESSO:
                    return prepare(orderInEvent, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(orderInEvent, 7);
                case CAPPUCCINO:
                    return prepare(orderInEvent, 9);
                default:
                    return prepare(orderInEvent, 11);
            }
        });
    }

    private Collection<Event> prepare(final OrderInEvent orderInEvent, int seconds) {

        // decrement the item in inventory
        try {
            inventory.decrementItem(orderInEvent.item);
        } catch (EightySixException e) {
            e.printStackTrace();
            logger.debug(orderInEvent.item + " is 86'd");
            return Arrays.asList(new EightySixEvent(orderInEvent.item));
        } catch (EightySixCoffeeException e) {
            // 86 both coffee items
            e.printStackTrace();
            logger.debug("coffee is 86'd");
            return Arrays.asList(
                    new EightySixEvent(Item.COFFEE_WITH_ROOM),
                    new EightySixEvent(Item.COFFEE_BLACK)
            );
        }

        // model the barista's time making the drink
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // return the completed drink
        return Arrays.asList(new OrderUpEvent(
                EventType.BEVERAGE_ORDER_UP,
                orderInEvent.orderId,
                orderInEvent.name,
                orderInEvent.item,
                orderInEvent.itemId,
                madeBy));
    }

    public void restockItem(Item item) {
        inventory.restock(item);
    }
}
