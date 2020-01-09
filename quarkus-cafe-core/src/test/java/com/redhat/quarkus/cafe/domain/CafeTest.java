package com.redhat.quarkus.cafe.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class CafeTest {

    @Inject
    Cafe cafe;

    @Test
    public void testOrderInBeverageOnly() throws ExecutionException, InterruptedException {

        List<Beverage> beverages = new ArrayList<>();
        List<Food> foods = new ArrayList<>();

        beverages.add(new Beverage());
        beverages.add(new Beverage());
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(), beverages, foods);
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

        List<Food> foods = new ArrayList<>();
        foods.add(new Food(Food.Type.MUFFIN));
        foods.add(new Food(Food.Type.CAKEPOP));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(), null, foods);
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

        List<Food> foods = new ArrayList<>();
        foods.add(new Food(Food.Type.MUFFIN));
        foods.add(new Food(Food.Type.CAKEPOP));
        List<Beverage> beverages = new ArrayList<>();
        beverages.add(new Beverage(Beverage.Type.CAPUCCINO));
        beverages.add(new Beverage(Beverage.Type.COFFEE_BLACK));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString(), beverages, foods);
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
