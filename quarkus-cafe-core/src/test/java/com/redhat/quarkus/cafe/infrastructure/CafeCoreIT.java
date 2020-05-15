package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(CafeCoreITResource.class)
public class CafeCoreIT extends KafkaIT {

    @Inject
    Cafe cafe;

    @BeforeAll
    public static void setUp() {
        consumerTopics = Arrays.asList("barista-in", "kitchen-in");
        producerTopics = Arrays.asList("web-in");
        setUpProducer();
        setUpConsumer();

        // give Kafka some time to start up
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            assertNull(e);
        }
    }


    @Test
    public void testOrderInBeveragesOnly() throws InterruptedException {

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);

        // send the order to Kafka and wait
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));
        Thread.sleep(1000);

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> newRecords = consumerMap.get("barista-in").poll(Duration.ofMillis(10000));

        // verify the number of new records
        assertEquals(2, newRecords.count());

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
            assertBeverageInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.ESPRESSO_DOUBLE) || orderInEvent.item.equals(Item.COFFEE_WITH_ROOM),
                    "The item should be either a " + Item.ESPRESSO_DOUBLE + " or a " + Item.COFFEE_WITH_ROOM + " not a " + orderInEvent.item);
        });
    }

    @Test
    public void testOrderInKitchenOnly() throws InterruptedException{

        List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Kirk"));
        menuItems.add(new LineItem(Item.MUFFIN, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, menuItems);

        // send the order to Kafka
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));

        Thread.sleep(2000);

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> newRecords = consumerMap.get("kitchen-in").poll(Duration.ofMillis(10000));

        // verify the number of new records
        assertEquals(2, newRecords.count());

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
            assertKitchenInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.CAKEPOP) || orderInEvent.item.equals(Item.MUFFIN),
                    "The item should be either a " + Item.MUFFIN + " or a " + Item.CAKEPOP + " not a " + orderInEvent.item);
        });
    }

    // saves a little typing
    void assertBeverageInEvent(final OrderInEvent orderInEvent) {
        assertEquals(EventType.BEVERAGE_ORDER_IN, orderInEvent.eventType, "This should be a " + EventType.BEVERAGE_ORDER_IN + " event not a " + orderInEvent.eventType);
    }
    void assertKitchenInEvent(final OrderInEvent orderInEvent) {
        assertEquals(EventType.KITCHEN_ORDER_IN, orderInEvent.eventType, "This should be a " + EventType.KITCHEN_ORDER_IN + " event not a " + orderInEvent.eventType);
    }
}
