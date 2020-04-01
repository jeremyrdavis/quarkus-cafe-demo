package com.redhat.quarkus.cafe.barista.infrastructure;

import java.util.Collections;
import java.util.Map;

import org.testcontainers.containers.KafkaContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    final KafkaContainer KAFKA = new KafkaContainer();

    @Override
    public Map<String, String> start() {
        KafkaContainer KAFKA = new KafkaContainer();
        KAFKA.start();
        System.setProperty("kafka.bootstrap.servers", KAFKA.getBootstrapServers());
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("kafka.bootstrap.servers");
        KAFKA.close();
    }

}