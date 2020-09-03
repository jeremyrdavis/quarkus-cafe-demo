package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class BaristaTest {

    @Inject
    Barista barista;

    Jsonb jsonb = JsonbBuilder.create();

    @BeforeEach
    public void restock() {
        barista.restockItem(Item.COFFEE_BLACK);
    }

    @Test
    public void testBlackCoffeeOrder() throws ExecutionException, InterruptedException {

        OrderInEvent orderInEvent = new OrderInEvent(EventType.BEVERAGE_ORDER_IN, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK);
        Collection<Event> events = barista.make(orderInEvent).get();
        await().atLeast(Duration.ofSeconds(5000));
        assertEquals(EventType.BEVERAGE_ORDER_UP, ((Event) (events.toArray()[0])).getEventType());
    }

}
