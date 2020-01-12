package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest @Testcontainers
public class BaristaIT extends BaseTestContainersIT {


    @Test
    public void testBlackCoffeeOrderInFromKafka() throws ExecutionException, InterruptedException {

        OrderInEvent beverageOrder = new OrderInEvent(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.BLACK_COFFEE);
        kafkaProducer.send(new ProducerRecord<>(CONSUMER_TOPIC, beverageOrder.orderId, jsonb.toJson(beverageOrder).toString())).get();
//        ConsumerRecords<String, String> initialRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
//        assertEquals(1, initialRecords.count());

        //Give the Barista time to make the drink
        System.out.println("wait for the Barista");
        Thread.sleep(7000);

        DescribeClusterResult describeClusterResult = kafkaAdminClient.describeCluster();

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertEquals(2, newRecords.count());
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.println("offset = %d, key = %s, value = %s\n"  + record.offset() + "\n" +
                    record.key() + "\n" + record.value());
//            assertEquals(beverageOrder.orderId, record.key());
//            assertEquals(beverageOrder.toString(), record.value());
            System.out.println(record.value().toString());
            BeverageOrder result = jsonb.fromJson(record.value(), BeverageOrder.class);
            assertEquals(EventType.BEVERAGE_ORDER_UP, result.eventType);
        }
    }
}

