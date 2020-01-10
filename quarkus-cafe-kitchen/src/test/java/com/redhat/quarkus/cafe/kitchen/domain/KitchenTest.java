package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.logging.Logger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class KitchenTest {
    static final Logger logger = Logger.getLogger(KitchenTest.class.getName());

    @Inject
    Kitchen kitchen;

    @Test
    public void testOrderCookie() {

        logger.info("Test that a Cookie is ready instantly");

        KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setMenuItem(MenuItem.COOKIE);
        kitchenOrder.setName("Jeremy");
        kitchenOrder.setOrderNumber("1234567");

        kitchen.orderIn(kitchenOrder);
        await()
                .atLeast(Duration.TWO_SECONDS)
                .atMost(Duration.FIVE_SECONDS);
    }

    @Test
    public void testOrderPanini() {

        logger.info("Test that a Panini takes 5 seconds");

        KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setMenuItem(MenuItem.PANINI);
        kitchenOrder.setName("Jeremy");
        kitchenOrder.setOrderNumber("1234567");

        kitchen.orderIn(kitchenOrder).thenAccept(result -> {

            assertEquals(OrderStatus.READY, result.getStatus());
        });

    }
}
