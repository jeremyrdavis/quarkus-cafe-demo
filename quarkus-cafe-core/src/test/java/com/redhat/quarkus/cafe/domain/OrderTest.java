package com.redhat.quarkus.cafe.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void testOrderCreatedEventFromBeveragesOnly() {

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(), beverages, null);
        System.out.println(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = Order.processCreateOrderCommand(createOrderCommand);
        assertNotNull(orderCreatedEvent);
        assertNotNull(orderCreatedEvent.events);
        assertEquals(2, orderCreatedEvent.events.size());
        orderCreatedEvent.events.forEach(e -> {
            assertEquals(OrderInEvent.class, e.getClass());
            assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
            assertEquals(EventType.BEVERAGE_ORDER_IN, e.eventType);
        });
    }

//    @Test
    public void testProcessCreateOrderCommandFoodOnly() {

        List<LineItem> foods = new ArrayList<>();
        foods.add(new LineItem(Item.MUFFIN, "Kirk"));
        foods.add(new LineItem(Item.CAKEPOP, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(),null, foods);
        OrderCreatedEvent orderCreatedEvent = Order.processCreateOrderCommand(createOrderCommand);

        assertNotNull(orderCreatedEvent);
        assertNotNull(orderCreatedEvent.events);
        assertEquals(2, orderCreatedEvent.events.size());
        orderCreatedEvent.events.forEach(e -> {
            assertEquals(OrderInEvent.class, e.getClass());
            assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
            assertEquals(EventType.KITCHEN_ORDER_IN, e.eventType);
        });
    }

    @Test
    public void testOrderInBeveragesAndFood() {

        List<LineItem> foods = new ArrayList<>();
        foods.add(new LineItem(Item.MUFFIN, "Kirk"));
        foods.add(new LineItem(Item.CAKEPOP, "Spock"));

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.CAPPUCCINO, "Kirk"));
        beverages.add(new LineItem(Item.COFFEE_BLACK, "Spock"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(), beverages, foods);
        OrderCreatedEvent orderCreatedEvent = Order.processCreateOrderCommand(createOrderCommand);

        assertNotNull(orderCreatedEvent);
        assertNotNull(orderCreatedEvent.events);
        assertEquals(4, orderCreatedEvent.events.size());
        int beveragOrders = 0;
        int kitchenOrders = 0;
        orderCreatedEvent.events.forEach(e -> {
            assertEquals(OrderInEvent.class, e.getClass());
            assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
        });
        assertEquals(2, orderCreatedEvent.events.stream().filter(
                e -> e.eventType.equals(EventType.KITCHEN_ORDER_IN)).count());
        assertEquals(2, orderCreatedEvent.events.stream().filter(
                e -> e.eventType.equals(EventType.BEVERAGE_ORDER_IN)).count());
    }

}
