package com.redhat.quarkus.cafe.barista.infrastructure;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

public class ExternalKafkaTestCase {

    static final String TOPIC_NAME = "orders";

    static KafkaProducer<String, String> kafkaProducer;
    static KafkaConsumer<String, String> kafkaConsumer;
    static AdminClient kafkaAdminClient;

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

    public void testConsumingMessages() {

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertFalse(records.isEmpty());

        assertEquals(5, records.count());
        records.forEach(stringStringConsumerRecord -> {
            assertEquals("testContainers", stringStringConsumerRecord.key());
            assertEquals("AreAwesome", stringStringConsumerRecord.value());
        });
    }

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

    static void setUpAdminClient() {

        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        kafkaAdminClient = AdminClient.create(config);
    }

    static void setUpTopics() {
        //create Topics

        List<NewTopic> topics = new ArrayList<>();
        topics.add(new NewTopic(TOPIC_NAME, 4, (short) 1));

        kafkaAdminClient.createTopics(topics);
        kafkaAdminClient.close();
    }

    static void setUpProducer(){
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

    static void setUpConsumer() {

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

/*
    @Test
    public void testSomething() throws InterruptedException, ExecutionException {

        ConsumerRecords<String, String> initialRecords = kafkaConsumer.poll(Duration.ofMillis(5000));

        if (initialRecords.count()==0) {
            System.out.println("No initial records found");
        }


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

        ConsumerRecords<String, String> records;

        final int giveUp = 5;
        int noRecordsCount = 0;

        while(true){

            records = kafkaConsumer.poll(Duration.ofMillis(5000));

            if (records.count()==0) {
                System.out.println("No records found");
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
        }

        assertFalse(records.isEmpty());

        assertEquals(records.count(), 1);
        records.forEach(stringStringConsumerRecord -> {
            assertEquals(stringStringConsumerRecord.key(), "testContainers");
            assertEquals(stringStringConsumerRecord.value(), "are awesome");
        });
    }
*/
}
