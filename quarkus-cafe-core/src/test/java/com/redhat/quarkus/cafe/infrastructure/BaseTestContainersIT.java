package com.redhat.quarkus.cafe.infrastructure;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.DockerComposeContainer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;
import java.util.*;

public abstract class BaseTestContainersIT {

    static DockerComposeContainer dockerComposeContainer;

    //Kafka stuff
    protected String PRODUCER_TOPIC = "orders";

    protected String CONSUMER_TOPIC = "orders";

    Jsonb jsonb = JsonbBuilder.create();

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

    @AfterAll
    public static void tearDownAll() {
        dockerComposeContainer.stop();
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

    void setUpAdminClient() {

        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        kafkaAdminClient = AdminClient.create(config);
    }

    void setUpTopics() {
        //create Topics

        List<NewTopic> topics = new ArrayList<>();
        topics.add(new NewTopic(PRODUCER_TOPIC, 4, (short) 1));
        topics.add(new NewTopic(CONSUMER_TOPIC, 4, (short) 1));

        kafkaAdminClient.createTopics(topics);
        kafkaAdminClient.close();
    }

    void setUpProducer(){
        //create Producer config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("input.topic.name", PRODUCER_TOPIC);

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
        kafkaConsumer.subscribe(Arrays.asList(CONSUMER_TOPIC));
    }
}
