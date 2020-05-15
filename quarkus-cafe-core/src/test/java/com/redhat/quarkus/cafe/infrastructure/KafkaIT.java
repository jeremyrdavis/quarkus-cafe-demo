package com.redhat.quarkus.cafe.infrastructure;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;

/**
 * Base Integration Test that sets up Kafka consumers and producers
 */
public abstract class KafkaIT {

    Jsonb jsonb = JsonbBuilder.create();

    protected static Collection<String> consumerTopics;

    protected static Collection<String> producerTopics;

    protected static Map<String, KafkaConsumer> consumerMap;

    protected static Map<String, KafkaProducer> producerMap;

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

            //initialize the Consumer
            KafkaConsumer kafkaConsumer = new KafkaConsumer(props);

            //subscribe
            kafkaConsumer.subscribe(Arrays.asList(topic));

            consumerMap.put(topic, kafkaConsumer);
        });
    }
}
