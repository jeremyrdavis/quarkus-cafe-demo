package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.OrderInEvent;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class KafkaService {

    private static final Logger logger = Logger.getLogger(KafkaService.class);

    private static final String TOPIC = "orders";

    @Inject
    private Vertx vertx;

    Jsonb jsonb = JsonbBuilder.create();

    private KafkaProducer<String, String> producer;

    public CompletableFuture<Void> produce(List<OrderInEvent> cafeEventList) {

        return CompletableFuture.runAsync(() -> {
            cafeEventList.stream().forEach(cafeEvent -> {
                logger.debug("\nSending:" + cafeEvent.toString());
                System.out.println(cafeEvent);
                KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(
                        TOPIC,
                        cafeEvent.itemId,
                        jsonb.toJson(cafeEvent));
                System.out.println(record);
                producer.send(record, res ->{
                    if (res.failed()) {
                        throw new RuntimeException(res.cause());
                    }
                });
            });
        });

    }

    @PostConstruct
    public void postConstruct() {
        // Config values can be moved to application.properties
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");
        producer = KafkaProducer.create(vertx, config);

    }
}
