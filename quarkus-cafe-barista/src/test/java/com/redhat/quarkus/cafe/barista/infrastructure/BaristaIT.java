package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import com.redhat.quarkus.cafe.domain.OrderUpEvent;
import com.redhat.quarkus.cafe.infrastructure.KafkaIT;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class BaristaIT extends KafkaIT {

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testOrderIn() {
        OrderInEvent orderIn = new OrderInEvent(EventType.BEVERAGE_ORDER_IN, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Lemmy", Item.COFFEE_BLACK);
        producerMap.get("barista-in").send(new ProducerRecord<>("barista-in", jsonb.toJson(orderIn)));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // Get the appropriate consumer, point to the first message, and pull all messages
        final KafkaConsumer baristaConsumer = consumerMap.get("orders");
        baristaConsumer.seekToBeginning(new ArrayList<TopicPartition>());
        final ConsumerRecords<String, String> baristaRecords = baristaConsumer.poll(Duration.ofMillis(1000));

        for (ConsumerRecord<String, String> record : baristaRecords) {
            System.out.println(record.value());
            OrderUpEvent orderUp = jsonb.fromJson(record.value(), OrderUpEvent.class);
            assertEquals(EventType.BEVERAGE_ORDER_UP, orderUp.eventType);
            assertEquals("Lemmy", orderUp.name);
            assertEquals(Item.COFFEE_BLACK, orderUp.item);
            assertNotNull(orderUp.madeBy);
        }
    }


}
