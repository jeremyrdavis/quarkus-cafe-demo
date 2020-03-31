package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Item;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.checkerframework.checker.units.qual.A;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@QuarkusTest
public class OrderInIT{

    Jsonb jsonb = JsonbBuilder.create();

    @Inject
    @Channel("orders-out")
    Emitter<String> orderEmitter;

/*
    @Inject
    KafkaResource kafkaResource;
*/

    @Container
    static DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(
            new File("src/test/resources/docker-compose.yaml"))
            .withExposedService("kafka", 9092)
            .withExposedService("zookeeper", 2181);

/*
    @BeforeEach
    public void setUp() {

        KafkaProducer kafkaProducer = createProducer();
        OrderEvent orderIn = new OrderEvent(UUID.randomUUID().toString(),"Moe", Item.COOKIE, UUID.randomUUID().toString(), EventType.KITCHEN_ORDER_IN);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("orders", orderIn.orderId, jsonb.toJson(orderIn));
        kafkaProducer.send(producerRecord);
    }
*/

    @AfterEach
    public void checkResults() {

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        KafkaConsumer kafkaConsumer = createConsumer();
        ConsumerRecords<String, String> moreRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertNotNull(moreRecords);
        for (ConsumerRecord<String, String> record : moreRecords) {
            System.out.printf("offset = %d, key = %s, value = %s\n",
                    record.offset(),
                    record.key(),
                    record.value());
        }
        assertEquals(2, moreRecords.count());
    }

    @Test
    //@Timeout(30)
    public void testOrderIn() throws InterruptedException {

        OrderEvent orderIn = new OrderEvent(UUID.randomUUID().toString(),"Moe", Item.COOKIE, UUID.randomUUID().toString(), EventType.KITCHEN_ORDER_IN);
        orderEmitter.send(jsonb.toJson(orderIn));
    }

    private KafkaProducer createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("input.topic.name", "orders");

        //initialize the Producer
        return new KafkaProducer(
                props,
                new StringSerializer(),
                new StringSerializer()
        );
    }

    private KafkaConsumer createConsumer() {

        //create Consumer config
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup" + new Random().nextInt());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        //initialize the Consumer
        KafkaConsumer kafkaConsumer = new KafkaConsumer(props);

        //subscribe
        kafkaConsumer.subscribe(Arrays.asList("orders"));
        return kafkaConsumer;
    }

}
