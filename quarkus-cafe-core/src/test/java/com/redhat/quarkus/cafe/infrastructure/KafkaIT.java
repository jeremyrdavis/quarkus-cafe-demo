package com.redhat.quarkus.cafe.infrastructure;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.*;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclBindingFilter;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Base Integration Test that sets up Kafka consumers and producers
 */
public abstract class KafkaIT {

    static final Logger logger = LoggerFactory.getLogger(KafkaIT.class);

    Jsonb jsonb = JsonbBuilder.create();

    protected static Collection<String> consumerTopics = Arrays.asList("barista-in", "kitchen-in");

    protected static Collection<String> producerTopics = Arrays.asList("web-in");

    protected static Collection<String> allTopics = Arrays.asList("web-in","barista-in", "kitchen-in");

    protected static Map<String, KafkaConsumer> consumerMap;

    protected static Map<String, KafkaProducer> producerMap;

    protected static AdminClient adminClient;

    protected KafkaConsumer baristaConsumer;

    protected KafkaConsumer kitchenConsumer;

    @BeforeEach
    public void beforeEach(){

        setUpAdminClient();

        Collection<NewTopic> newTopics = new ArrayList<>();
        allTopics.forEach(t -> {
            newTopics.add(new NewTopic(t, 1, (short) 1));
        });
        adminClient.createTopics(newTopics);

        setUpProducer();
        setUpConsumer();

        // give Kafka some time to start up
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            assertNull(e);
        }
    }

    @AfterEach
    public void afterEach(){

        adminClient.deleteTopics(allTopics);
    }

    private void setUpAdminClient() {

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_BOOTSTRAP_URLS"));
        adminClient = AdminClient.create(props);
    }

    protected static void setUpProducer() {

        // we need 1 producer per topic
        producerMap = new HashMap<>(producerTopics.size());

        // create a producer for each topic
        producerTopics.forEach(topic -> {
            //create Producer config
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("KAFKA_BOOTSTRAP_URLS"));
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put("input.topic.name", topic);

            //initialize the Producer
            KafkaProducer kafkaProducer = new KafkaProducer(
                    props,
                    new StringSerializer(),
                    new StringSerializer()
            );

            producerMap.put(topic, kafkaProducer);
        });
    }

    protected static void setUpConsumer() {

        consumerMap = new HashMap<>(consumerTopics.size());

        consumerTopics.forEach(topic -> {

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
            props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 52428800);

            //initialize the Consumer
            KafkaConsumer kafkaConsumer = new KafkaConsumer(props);

            //subscribe
            kafkaConsumer.subscribe(Arrays.asList(topic));

            consumerMap.put(topic, kafkaConsumer);
        });
    }
}
