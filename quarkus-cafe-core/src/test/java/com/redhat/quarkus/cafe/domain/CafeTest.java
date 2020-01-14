package com.redhat.quarkus.cafe.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CafeTest {

    @Inject
    Cafe cafe;

    @Test
    public void testOrderInBeverageOnly() throws ExecutionException, InterruptedException {

        List<Order> beverages = new ArrayList<>();

        beverages.add(new Order(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new Order(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);
        cafe.orderIn(createOrderCommand).thenApply(cafeEvents -> {
            assertNotNull(cafeEvents);
            assertEquals(2, cafeEvents.size());
            cafeEvents.stream().forEach(e -> {
                assertEquals(BeverageOrderInEvent.class, e.getClass());
            });
            return null;
        });
    }

    @Test
    public void testOrderInFoodOnly() {

        List<Order> foods = new ArrayList<>();
        foods.add(new Order(Item.MUFFIN, "Kirk"));
        foods.add(new Order(Item.CAKEPOP, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, foods);
        cafe.orderIn(createOrderCommand).thenApply(cafeEvents -> {
            assertNotNull(cafeEvents);
            assertEquals(2, cafeEvents.size());
            cafeEvents.stream().forEach(e -> {
                assertEquals(BeverageOrderInEvent.class, e.getClass());
            });
            return null;
        });
    }

    @Test
    public void testOrderInBeveragesAndFood() {

        List<Order> foods = new ArrayList<>();
        foods.add(new Order(Item.MUFFIN, "Kirk"));
        foods.add(new Order(Item.CAKEPOP, "Spock"));

        List<Order> beverages = new ArrayList<>();
        beverages.add(new Order(Item.CAPPUCCINO, "Kirk"));
        beverages.add(new Order(Item.COFFEE_BLACK, "Spock"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, foods);
        cafe.orderIn(createOrderCommand).thenApply(cafeEvents -> {
            assertNotNull(cafeEvents);
            assertEquals(2, cafeEvents.size());
            cafeEvents.stream().forEach(e -> {
                assertNotNull(cafeEvents);
                assertEquals(4, cafeEvents.size());
                assertEquals(2, cafeEvents.stream().filter(be -> be.getClass().equals(BeverageOrderInEvent.class)).count());
                assertEquals(2, cafeEvents.stream().filter(ke -> ke.getClass().equals(KitchenOrderInEvent.class)).count());
            });
            return null;
        });
    }
}
