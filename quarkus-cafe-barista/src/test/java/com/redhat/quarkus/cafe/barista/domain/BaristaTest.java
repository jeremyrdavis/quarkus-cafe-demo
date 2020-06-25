package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.barista.infrastructure.Barista;
import com.redhat.quarkus.cafe.barista.infrastructure.KafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class BaristaTest {

    @Inject
    Barista barista;

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testBlackCoffeeOrder() throws ExecutionException, InterruptedException {

        OrderIn order = new OrderIn(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK);
        CompletableFuture<OrderUp> result = barista.processOrderIn2(order);
        OrderUp orderUp = result.get();
        await().atLeast(Duration.ofSeconds(5000));
        assertEquals(EventType.BEVERAGE_ORDER_UP, orderUp.eventType);
        assertNotNull(orderUp.madeBy);
    }

}
