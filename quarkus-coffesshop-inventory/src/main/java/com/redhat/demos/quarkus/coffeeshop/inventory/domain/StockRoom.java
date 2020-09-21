package com.redhat.demos.quarkus.coffeeshop.inventory.domain;

import com.redhat.quarkus.cafe.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class StockRoom {

    static final Logger logger = LoggerFactory.getLogger(StockRoom.class);

    public CompletableFuture<CoffeeshopCommand> handleRestockItemCommand(final Item item) {

        logger.debug("restocking: {}", item);

        return CompletableFuture.supplyAsync(() -> {

            switch (item) {
                case COFFEE_BLACK:
                    return restockBarista(item, 10);
                case COFFEE_WITH_ROOM:
                    return restockBarista(item, 10);
                case ESPRESSO:
                    return restockBarista(item, 10);
                case ESPRESSO_DOUBLE:
                    return restockBarista(item, 10);
                case CAPPUCCINO:
                    return restockBarista(item, 10);
                default:
                    return restockBarista(item, 10);
            }
        });
    }

    private void sleep(int seconds) {
        // model the time to restock
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private CoffeeshopCommand restockBarista(Item item, int seconds) {
        sleep(seconds);
        return new RestockBaristaCommand(item, 99);
    }

    private CoffeeshopCommand restockKitchen(Item item, int seconds) {
        sleep(seconds);
        return new RestockKitchenCommand(item, 99);
    }

}
