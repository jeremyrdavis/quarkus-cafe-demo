package com.redhat.quarkus.cafe.infrastructure;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;

import java.util.Collections;
import java.util.Map;

public class CafeCoreITResource implements QuarkusTestResourceLifecycleManager {

    final KafkaContainer KAFKA = new KafkaContainer();

    final GenericContainer MONGO = new GenericContainer("mongo:4.0").withExposedPorts(27017);

    @Override
    public Map<String, String> start() {
        KafkaContainer KAFKA = new KafkaContainer();
        KAFKA.start();
        System.setProperty("KAFKA_BOOTSTRAP_URLS", KAFKA.getBootstrapServers());
        MONGO.start();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("KAFKA_BOOTSTRAP_URLS");
        MONGO.close();
        KAFKA.close();
    }
}
