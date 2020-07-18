package com.redhat.quarkus.cafe.infrastructure;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;

import java.util.Collections;
import java.util.Map;

public class MongoITResource implements QuarkusTestResourceLifecycleManager {

    static final GenericContainer MONGO = new GenericContainer("mongo:4.0").withExposedPorts(27017);

    @Override
    public Map<String, String> start() {
        MONGO.start();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        System.clearProperty("KAFKA_BOOTSTRAP_URLS");
        MONGO.close();
    }
}
