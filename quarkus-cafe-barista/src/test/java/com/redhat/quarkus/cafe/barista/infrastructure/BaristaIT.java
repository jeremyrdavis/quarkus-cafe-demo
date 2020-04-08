package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest @QuarkusTestResource(KafkaTestResource.class)
public class BaristaIT {

    static KafkaProducer<String, String> kafkaProducer;
    static KafkaConsumer<String, String> kafkaConsumer;

    Jsonb jsonb = JsonbBuilder.create();

    @BeforeAll
    public static void setUp() {

        createKafkaProducer();
        createKafkaConsumer();
    }

    @AfterAll
    public static void tearDown() {
        kafkaProducer.close();
        kafkaConsumer.close();
    }

    @Test
    public void testBlackCoffeeOrderInFromKafka() throws ExecutionException, InterruptedException {

        BeverageOrder beverageOrder = new BeverageOrder(EventType.BEVERAGE_ORDER_IN, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK);
        kafkaProducer.send(new ProducerRecord<>("orders", beverageOrder.orderId, jsonb.toJson(beverageOrder).toString())).get();

        Thread.sleep(10000);

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.println("offset = %d, key = %s, value = %s "  + record.offset() + " " +  record.key() + "\n" + record.value());
            BeverageOrder result = jsonb.fromJson(record.value(), BeverageOrder.class);
            assertEquals(beverageOrder.orderId, result.orderId);

        }
        assertEquals(2, newRecords.count());
    }

    private static void createKafkaProducer() {

        //create Producer config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("kafka.bootstrap.servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("input.topic.name", "orders");

        //initialize the Producer
        kafkaProducer = new KafkaProducer(
                props,
                new StringSerializer(),
                new StringSerializer()
        );
    }

    private static void createKafkaConsumer() {
        //setup consumer
        //create Consumer config
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("kafka.bootstrap.servers"));
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup" + new Random().nextInt());
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        //initialize the Consumer
        kafkaConsumer = new KafkaConsumer(consumerProps);

        //subscribe
        kafkaConsumer.subscribe(Arrays.asList("orders"));
    }


}

