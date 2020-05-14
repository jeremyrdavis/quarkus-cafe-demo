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

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class CafeCoreIT {

    @Inject
    Cafe cafe;

    static Jsonb jsonb = JsonbBuilder.create();

    static KafkaConsumer kafkaConsumer;

    @BeforeAll
    public static void setUp() {

        //create Consumer config
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_BOOTSTRAP_URLS"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup" + new Random().nextInt());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        //initialize the Consumer
        kafkaConsumer = new KafkaConsumer(props);

        //subscribe
        kafkaConsumer.subscribe(Arrays.asList("barista-in"));
    }

    @Test
    public void testOrderInBeveragesOnly() {

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
/*
    {
        "eventType":"BEVERAGE_ORDER_IN",
        "item":"COFFEE_WITH_ROOM",
        "itemId":"be038e24-1934-49ae-a0dd-335b6f7a7401",
        "name":"Kirk",
        "orderId":"5ebd7f9a5d2778473d73dd31"}

*/
            OrderInEvent orderEvent = jsonb.fromJson(record.value(), OrderInEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                if (orderEvent.item.equals(Item.COFFEE_WITH_ROOM) || orderEvent.item.equals(Item.ESPRESSO_DOUBLE))
                    beverageOrderInCount++;
            }
        }
        assertEquals(2, beverageOrderInCount);
    }
}
