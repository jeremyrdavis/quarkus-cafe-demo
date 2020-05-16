package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(CafeITResource.class)
public class CafeCoreIT extends KafkaIT {

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
        ConsumerRecords<String, String> newRecords = consumerMap.get("barista-in").poll(Duration.ofMillis(1000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
//            assertBeverageInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.ESPRESSO_DOUBLE) || orderInEvent.item.equals(Item.COFFEE_WITH_ROOM),
                    "The item should be either a " + Item.ESPRESSO_DOUBLE + " or a " + Item.COFFEE_WITH_ROOM + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        assertEquals(2, newRecords.count());
    }

    @Test
    public void testOrderInKitchenOnly() throws InterruptedException{

        List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Mickey"));
        menuItems.add(new LineItem(Item.MUFFIN, "Goofy"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, menuItems);

        // send the order to Kafka
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));

        Thread.sleep(2000);

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> newRecords = consumerMap.get("kitchen-in").poll(Duration.ofMillis(5000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
//            assertKitchenInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.CAKEPOP) || orderInEvent.item.equals(Item.MUFFIN),
                    "The item should be either a " + Item.MUFFIN + " or a " + Item.CAKEPOP + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        assertEquals(2, newRecords.count());

    }

    @Test
    public void testOrderInBeveragesAndKitchen() {

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Harry"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Ron"));

        List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Harry"));
        menuItems.add(new LineItem(Item.MUFFIN, "Hermione"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, menuItems);

        // send the order to Kafka
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> baristaRecords = consumerMap.get("barista-in").poll(Duration.ofMillis(5000));
        ConsumerRecords<String, String> kitchenRecords = consumerMap.get("kitchen-in").poll(Duration.ofMillis(5000));

        // verify that the records are of the correct type
        baristaRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
/*
            assertBeverageInEvent(orderInEvent);
*/
            assertTrue(orderInEvent.item.equals(Item.ESPRESSO_DOUBLE) || orderInEvent.item.equals(Item.COFFEE_WITH_ROOM),
                    "The item should be either a " + Item.ESPRESSO_DOUBLE + " or a " + Item.COFFEE_WITH_ROOM + " not a " + orderInEvent.item);
        });

        // verify that the records are of the correct type
        kitchenRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
/*
            assertKitchenInEvent(orderInEvent);
*/
            assertTrue(orderInEvent.item.equals(Item.CAKEPOP) || orderInEvent.item.equals(Item.MUFFIN),
                    "The item should be either a " + Item.MUFFIN + " or a " + Item.CAKEPOP + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        assertEquals(2, baristaRecords.count());
        // verify the number of new records
        assertEquals(2, kitchenRecords.count());


    }

}
