package com.redhat.quarkus.cafe.barista.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Testcontainers
public class DockerComposeBaristaIT {

    final String TOPIC_NAME = "orders";
    static DockerComposeContainer dockerComposeContainer;
    KafkaProducer<String, String> kafkaProducer;
    KafkaConsumer<String, String> kafkaConsumer;
    AdminClient kafkaAdminClient;

    @BeforeAll
    public static void setUpAll() {
        dockerComposeContainer = new DockerComposeContainer(
                new File("src/test/resources/docker-compose.yaml"))
                .withExposedService("kafka", 9092)
                .withExposedService("zookeeper", 2181);
        dockerComposeContainer.start();
    }

    @BeforeEach
    public void setUp() {

        setUpAdminClient();
        setUpProducer();
        setUpTopics();
        setUpConsumer();
    }

    @AfterEach
    public void tearDown() {
    }

    @AfterAll
    public static void tearDownAll() {
        dockerComposeContainer.stop();
    }

    @Test
    public void testConsumingMessages() {

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertFalse(records.isEmpty());

        assertEquals(5, records.count());
        records.forEach(stringStringConsumerRecord -> {
            assertEquals("testContainers", stringStringConsumerRecord.key());
            assertEquals("AreAwesome", stringStringConsumerRecord.value());
        });
    }

    @Test
    public void testSendingMessages() {

        long numberOfEvents = 5;
        for (int i = 0; i < numberOfEvents; i++) {
            String key = "testContainers";
            String value = "AreAwesome";
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, key, value);
            System.out.println("ProducerRecord:" + record.toString());
            try {
                RecordMetadata recordMetadata = kafkaProducer.send(record).get();
                assertNotNull(recordMetadata);
                System.out.println("Offset:" + recordMetadata.offset());
                System.out.println("RecordMetadata:" + recordMetadata.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        kafkaProducer.close();
    }

    void setUpAdminClient() {

        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        kafkaAdminClient = AdminClient.create(config);
    }

    void setUpTopics() {
        //create Topics

        List<NewTopic> topics = new ArrayList<>();
        topics.add(new NewTopic(TOPIC_NAME, 4, (short) 1));

        kafkaAdminClient.createTopics(topics);
        kafkaAdminClient.close();
    }

    void setUpProducer(){
        //create Producer config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("input.topic.name", TOPIC_NAME);

        //initialize the Producer
        kafkaProducer = new KafkaProducer(
                props,
                new StringSerializer(),
                new StringSerializer()
        );
    }

    void setUpConsumer() {

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
        kafkaConsumer = new KafkaConsumer(props);
        //subscribe
        kafkaConsumer.subscribe(singletonList(TOPIC_NAME));
    }

}
