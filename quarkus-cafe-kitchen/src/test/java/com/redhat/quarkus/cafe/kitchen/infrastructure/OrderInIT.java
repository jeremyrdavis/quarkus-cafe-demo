package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Item;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest @Testcontainers
public class OrderInIT extends BaseTestContainersIT{

    @Inject
    KafkaResource kafkaResource;

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    @Timeout(30)
    public void testOrderIn() throws InterruptedException {

        OrderEvent orderIn = new OrderEvent(UUID.randomUUID().toString(),"Moe", Item.PANINI, UUID.randomUUID().toString(), EventType.KITCHEN_ORDER_IN);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("orders", orderIn.orderId, jsonb.toJson(orderIn));
        kafkaProducer.send(producerRecord);

        System.out.println("\nSleeping for 20 seconds\n");
        Thread.sleep(20000);

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertNotNull(newRecords);
        assertEquals(2, newRecords.count());
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.printf("offset = %d, key = %s, value = %s\n",
                    record.offset(),
                    record.key(),
                    record.value());
        }
    }

}
