package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class KafkaResource {

    private static final String TOPIC = "orders";

    Logger logger = Logger.getLogger(KafkaResource.class);

    @Inject
    Kitchen kitchen;

    @Inject
    private Vertx vertx;

    private KafkaProducer<String, String> producer;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orderin")
    public void orderIn(String message) {

        System.out.println("\nmessage received:\n" + message);
        logger.debug("\nOrder Received:\n" + message);

        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        if (eventType.equals(EventType.KITCHEN_ORDER_IN.toString())) {

            logger.debug("\nKitchen Order In Received:\n");

            OrderEvent orderEvent = jsonb.fromJson(message, OrderEvent.class);
            onKitchenOrderIn(orderEvent).thenApply(res -> {
                updateKafka(res);
                return null;
            });
        }

    }
//    @Outgoing("kitchen-orders-up")
    private CompletionStage<OrderEvent> onKitchenOrderIn(final OrderEvent orderEvent) {

        return kitchen.orderIn(orderEvent);
    }

    private void updateKafka(final OrderEvent orderEvent) {
        System.out.println("\nNow update Kafka!");
        logger.debug("\nSending:" + orderEvent.toString());
        System.out.println(orderEvent);
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(
                TOPIC,
                orderEvent.itemId,
                jsonb.toJson(orderEvent));
        System.out.println(record);
        producer.send(record, res ->{
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
        });
    }

    @PostConstruct
    public void postConstruct() {
        // Config values can be moved to application.properties
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "my-cluster-kafka-bootstrap.kafka:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");
        producer = KafkaProducer.create(vertx, config);
    }


}
