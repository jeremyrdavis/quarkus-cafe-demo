package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.EventType;
import com.redhat.quarkus.cafe.barista.domain.Item;
import com.redhat.quarkus.cafe.barista.domain.OrderIn;
import com.redhat.quarkus.cafe.barista.domain.OrderUp;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest @QuarkusTestResource(KafkaTestResource.class)
public class BaristaIT {

    Jsonb jsonb = JsonbBuilder.create();

    static KafkaConsumer<String, String> kafkaConsumer;

    static KafkaProducer<String, String> kafkaProducer;

    @Test
    public void testOrderIn() {
        OrderIn orderIn = new OrderIn(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Lemmy", Item.COFFEE_BLACK);
        kafkaProducer.send(new ProducerRecord<>("orders", jsonb.toJson(orderIn)));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.println(record.value());
            OrderUp orderUp = jsonb.fromJson(record.value(), OrderUp.class);
            assertEquals(EventType.BEVERAGE_ORDER_UP, orderUp.eventType);
            assertEquals("Lemmy", orderUp.name);
            assertEquals(Item.COFFEE_BLACK, orderUp.item);
        }
    }


    @BeforeAll
    public static void setUp() {
        setUpProducer();
        setUpConsumer();
    }

    protected static void setUpProducer() {
        //create Producer config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_BOOTSTRAP_URLS"));
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

    protected static void setUpConsumer() {

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
}
