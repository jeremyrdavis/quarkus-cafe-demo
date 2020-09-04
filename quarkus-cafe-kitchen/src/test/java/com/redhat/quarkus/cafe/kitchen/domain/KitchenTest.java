package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import com.redhat.quarkus.cafe.domain.*;
import javax.inject.Inject;
import java.util.Collection;
import java.util.EventListener;
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

        logger.info("Test that a Cakepop is ready instantly");

        OrderInEvent orderIn = new OrderInEvent(
                EventType.KITCHEN_ORDER_IN,
                UUID.randomUUID().toString(),
                "Moe",
                Item.CAKEPOP);

        CompletableFuture<Collection<Event>> result = kitchen.make(orderIn);
        Collection<Event> events = result.get();
        assertEquals(1, events.size());
        events.forEach(e -> {
            assertEquals(EventType.KITCHEN_ORDER_UP, e.getEventType());
            OrderUpEvent orderUpEvent = (OrderUpEvent) e;
            assertEquals(orderIn.item, orderUpEvent.item);
            assertEquals(orderIn.orderId, orderUpEvent.orderId);
            assertEquals(orderIn.name, orderUpEvent.name);
            assertEquals(EventType.KITCHEN_ORDER_UP, orderUpEvent.eventType);
        });
    }
}
