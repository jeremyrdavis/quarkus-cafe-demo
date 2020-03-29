package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;
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

        OrderEvent orderIn = new OrderEvent(UUID.randomUUID().toString(), "Moe", Item.COOKIE, UUID.randomUUID().toString(), EventType.KITCHEN_ORDER_IN);

        kitchen.orderIn(orderIn);
        await()
                .atLeast(Duration.TWO_SECONDS)
                .atMost(Duration.FIVE_SECONDS);
    }

    @Test
    public void testOrderPanini() {

        logger.info("Test that a Panini takes 5 seconds");

        OrderEvent orderIn = new OrderEvent(UUID.randomUUID().toString(), "Moe", Item.PANINI, UUID.randomUUID().toString(), EventType.KITCHEN_ORDER_IN);

        kitchen.orderIn(orderIn).thenAccept(result -> {

            assertEquals(EventType.KITCHEN_ORDER_UP, result.eventType);
        });

    }
}
