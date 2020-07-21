package com.redhat.quarkus.cafe.kitchen.domain;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import com.redhat.quarkus.cafe.domain.OrderUpEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class KitchenTest {

    static final Logger logger = Logger.getLogger(KitchenTest.class.getName());

    @Inject
    Kitchen kitchen;

    @Test
    public void testOrderCakepop() throws ExecutionException, InterruptedException {

        logger.info("Test that a Cookie is ready instantly");

        OrderInEvent orderIn = new OrderInEvent(
                EventType.KITCHEN_ORDER_IN,
                UUID.randomUUID().toString(),
                "Moe",
                Item.CAKEPOP);

        CompletableFuture<OrderUpEvent> result = kitchen.make(orderIn);
        OrderUpEvent orderUp = result.get();
        assertEquals(orderIn.item, orderUp.item);
        assertEquals(orderIn.orderId, orderUp.orderId);
        assertEquals(orderIn.name, orderUp.name);
        assertEquals(EventType.KITCHEN_ORDER_UP, orderUp.eventType);
    }
}
