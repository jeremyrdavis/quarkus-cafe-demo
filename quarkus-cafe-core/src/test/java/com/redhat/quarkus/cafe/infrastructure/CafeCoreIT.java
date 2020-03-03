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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
            System.out.println("beverages only");
            System.out.println("offset = %d, key = %s, value = %s\n"  + record.offset() + "\n" +
                    record.key() + "\n" + record.value());
            System.out.println(record.value().toString());
            OrderEvent orderEvent = jsonb.fromJson(record.value(), OrderEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
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
            System.out.println("kitchen only");
/*
            System.out.println("offset = %d, key = %s, value = %s\n"  + record.offset() + "\n" +
                    record.key() + "\n" + record.value());
*/
            OrderEvent orderEvent = jsonb.fromJson(record.value(), OrderEvent.class);
            if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                kitchenOrderInCount++;
            }
        }
        assertEquals(2, kitchenOrderInCount);
    }
}
