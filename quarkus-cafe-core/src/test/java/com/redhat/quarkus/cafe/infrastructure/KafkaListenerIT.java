package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.OrderEvent;
import com.redhat.quarkus.cafe.domain.OrderUpEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest @Testcontainers
public class KafkaListenerIT extends BaseTestContainersIT{

    public KafkaListenerIT() {
        super("orders", "webui");
    }

    @Test
    public void testReceivingOrders() {

        OrderUpEvent beverageOrderUpEvent = new OrderUpEvent(UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK, EventType.BEVERAGE_ORDER_UP);
        try {
            kafkaProducer.send(new ProducerRecord<>(CONSUMER_TOPIC, beverageOrderUpEvent.orderId, jsonb.toJson(beverageOrderUpEvent).toString())).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertNull(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            assertNull(e);
        }

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertEquals(1, newRecords.count());
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.println("offset = %d, key = %s, value = %s\n"  + record.offset() + "\n" +
                    record.key() + "\n" + record.value());
            OrderEvent result = jsonb.fromJson(record.value(), OrderEvent.class);
            assertEquals(EventType.BEVERAGE_ORDER_UP, result.eventType);
            assertEquals(result.orderId, record.key());
            assertEquals("Jeremy", result.name);
            assertEquals(Item.COFFEE_BLACK, result.item);
            assertEquals(jsonb.toJson(result).toString(), record.value());
            System.out.println(record.value().toString());
        }

    }
}
