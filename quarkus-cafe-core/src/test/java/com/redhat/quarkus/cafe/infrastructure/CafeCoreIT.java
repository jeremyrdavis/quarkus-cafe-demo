package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CafeCoreIT extends BaseTestContainersIT{

    static final String PRODUCER_TOPIC = "orders-test";

    static final String CONSUMER_TOPIC = "orders-test";

    @Inject
    CafeCore cafeCore;

    public CafeCoreIT() {
        super(PRODUCER_TOPIC, CONSUMER_TOPIC);
    }

    @Test
    public void testOrderInBeveragesOnly() {

        List<Order> beverages = new ArrayList<>();

        beverages.add(new Order(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new Order(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);

        try {
            cafeCore.orderIn(createOrderCommand);
        } catch (ExecutionException e) {
            assertNull(e);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // We'll track the number of actual events
        int beverageOrderInCount = 0;

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            OrderEvent orderEvent = jsonb.fromJson(record.value(), OrderEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                if(orderEvent.item.equals(Item.COFFEE_WITH_ROOM)||orderEvent.item.equals(Item.ESPRESSO_DOUBLE))
                beverageOrderInCount++;
            }
        }
        assertEquals(2, beverageOrderInCount);
    }

    @Test
    public void testOrderInKitchenOnly() {

        List<Order> foods = new ArrayList<>();
        foods.add(new Order(Item.MUFFIN, "Kirk"));
        foods.add(new Order(Item.CAKEPOP, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, foods);

        try {
            cafeCore.orderIn(createOrderCommand);
        } catch (ExecutionException e) {
            assertNull(e);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // We'll track the number of actual events
        int kitchenOrderInCount = 0;

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            OrderEvent orderEvent = jsonb.fromJson(record.value(), OrderEvent.class);
            if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                if(orderEvent.item.equals(Item.MUFFIN)||orderEvent.item.equals(Item.CAKEPOP))
                kitchenOrderInCount++;
            }
        }
        assertEquals(2, kitchenOrderInCount);
    }

    @Test
    public void testOrderInBeverageAndKitchen() {

        List<Order> beverages = new ArrayList<>();
        beverages.add(new Order(Item.COFFEE_BLACK, "Kirk"));
        beverages.add(new Order(Item.CAPPUCCINO, "Spock"));

        List<Order> foods = new ArrayList<>();
        foods.add(new Order(Item.CROISSANT, "Kirk"));
        foods.add(new Order(Item.CROISSANT_CHOCOLATE, "Spock"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, foods);

        try {
            cafeCore.orderIn(createOrderCommand);
        } catch (ExecutionException e) {
            assertNull(e);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // We'll track the number of actual events
        int kitchenOrderInCount = 0;
        int baristaOrderInCount = 0;


        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));

        assertTrue(newRecords.count() >= 4);
        for (ConsumerRecord<String, String> record : newRecords) {
            OrderEvent orderEvent = jsonb.fromJson(record.value(), OrderEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                if(orderEvent.item.equals(Item.COFFEE_BLACK)||orderEvent.item.equals(Item.CAPPUCCINO))
                baristaOrderInCount++;
            }else if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                if(orderEvent.item.equals(Item.CROISSANT)||orderEvent.item.equals(Item.CROISSANT_CHOCOLATE))
                kitchenOrderInCount++;
            }
        }
        assertEquals(2, baristaOrderInCount);
        assertEquals(2, kitchenOrderInCount);
    }
}
