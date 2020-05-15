package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(CafeCoreITResource.class)
public class CafeCoreIT extends KafkaIT {

    @Inject
    Cafe cafe;

    static Jsonb jsonb = JsonbBuilder.create();

    @BeforeAll
    public static void setUp() {
        producerTopics = Arrays.asList("barista-in", "kitchen-in");
        consumerTopics = Arrays.asList("web-in");
        setUpProducer();
        setUpConsumer();
    }


    @Test
    public void testOrderInBeveragesOnly() {

        assertTrue(false);
/*
        List<LineItem> beverages = new ArrayList<>();

        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);

        cafe.handleCreateOrderCommand(createOrderCommand);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            assertNull(e);
        }
        int beverageOrderInCount = 0;

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.println(record.value());
    {
        "eventType":"BEVERAGE_ORDER_IN",
        "item":"COFFEE_WITH_ROOM",
        "itemId":"be038e24-1934-49ae-a0dd-335b6f7a7401",
        "name":"Kirk",
        "orderId":"5ebd7f9a5d2778473d73dd31"}

            OrderInEvent orderEvent = jsonb.fromJson(record.value(), OrderInEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                if (orderEvent.item.equals(Item.COFFEE_WITH_ROOM) || orderEvent.item.equals(Item.ESPRESSO_DOUBLE))
                    beverageOrderInCount++;
            }
        }
        assertEquals(2, beverageOrderInCount);
*/
    }
}
