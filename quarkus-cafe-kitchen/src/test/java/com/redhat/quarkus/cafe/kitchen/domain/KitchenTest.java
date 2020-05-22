package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class KitchenTest {

    static final Logger logger = Logger.getLogger(KitchenTest.class.getName());

    @Inject
    Kitchen kitchen;

    @Test
    public void testOrderCookie() throws ExecutionException, InterruptedException {

        logger.info("Test that a Cookie is ready instantly");

        OrderIn orderIn = new OrderIn(UUID.randomUUID().toString(), "Moe", Item.COOKIE, UUID.randomUUID().toString());

        CompletableFuture<OrderUp> result = kitchen.processOrderIn(orderIn);
        OrderUp orderUp = result.get();
        assertEquals(orderIn.item, orderUp.item);
        assertEquals(orderIn.itemId, orderUp.itemId);
        assertEquals(orderIn.name, orderUp.name);
        assertEquals(EventType.KITCHEN_ORDER_UP, orderUp.eventType);
    }
}
