package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.Assert.*;

@QuarkusTest @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTest {

    @Inject
    Inventory inventory;

    @Test @Order(1)
    public void testStockIsPopulated() {

        Map<Item, Integer> inStock = inventory.getStock();
        assertNotNull(inStock);
        inStock.forEach((k,v) -> {
            System.out.println(k + " " + v);
        });
    }

    @Test @Order(2)
    public void testDecrementCoffee() {

        Integer totalCoffee = inventory.getTotalCoffee();
        try {
            inventory.decrementItem(Item.COFFEE_WITH_ROOM);
        } catch (EightySixCoffeeException | EightySixException e) {
            // Items are initiated with a minimum value of 30
            assertNull(e);
        }
        Integer updatedCoffee = inventory.getTotalCoffee();
        assertTrue(updatedCoffee == totalCoffee - 1);
    }

    @Test @Order(3)
    public void testEightySixCoffee() {

        Integer totalCoffee = inventory.getTotalCoffee();
        for (int i = 0; i < totalCoffee; i++) {
            try {
                inventory.decrementItem(Item.COFFEE_BLACK);
            } catch (Exception e) {
                assertEquals(EightySixCoffeeException.class, e.getClass());
                assertEquals(totalCoffee, Integer.valueOf(i));
            }
        }
    }
}
