package com.redhat.quarkus.cafe.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CafeTest {

    Cafe cafe = new Cafe();

    @Test
    public void testOrderInBeverageOnly() throws ExecutionException, InterruptedException {

        List<LineItem> beverages = new ArrayList<>();

        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);
        List<LineItemEvent> orderEvents = cafe.orderIn(createOrderCommand);
        assertNotNull(orderEvents);
        assertEquals(2, orderEvents.size());
        orderEvents.stream().forEach(e -> {
                assertEquals(BeverageLineItemInEvent.class, e.getClass());
        });
    }

    @Test
    public void testOrderInFoodOnly() throws ExecutionException, InterruptedException {

        List<LineItem> foods = new ArrayList<>();
        foods.add(new LineItem(Item.MUFFIN, "Kirk"));
        foods.add(new LineItem(Item.CAKEPOP, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, foods);
        List<LineItemEvent> orderEvents = cafe.orderIn(createOrderCommand);
        assertNotNull(orderEvents);
        assertEquals(2, orderEvents.size());
        orderEvents.stream().forEach(e -> {
            assertEquals(KitchenLineItemInEvent.class, e.getClass());
        });
    }

    @Test
    public void testOrderInBeveragesAndFood() throws ExecutionException, InterruptedException {

        List<LineItem> foods = new ArrayList<>();
        foods.add(new LineItem(Item.MUFFIN, "Kirk"));
        foods.add(new LineItem(Item.CAKEPOP, "Spock"));

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.CAPPUCCINO, "Kirk"));
        beverages.add(new LineItem(Item.COFFEE_BLACK, "Spock"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, foods);
        List<LineItemEvent> orderEvents = cafe.orderIn(createOrderCommand);
        assertNotNull(orderEvents);
        assertEquals(4, orderEvents.size());
        assertEquals(2, orderEvents.stream().filter(be -> be.getClass().equals(BeverageLineItemInEvent.class)).count());
        assertEquals(2, orderEvents.stream().filter(ke -> ke.getClass().equals(KitchenLineItemInEvent.class)).count());
/*
        orderEvents.stream().forEach(e -> {
            assertEquals(BeverageOrderInEvent.class, e.getClass());
            assertEquals(2, orderEvents.stream().filter(be -> be.getClass().equals(BeverageOrderInEvent.class)).count());
        });
*/
    }
}
