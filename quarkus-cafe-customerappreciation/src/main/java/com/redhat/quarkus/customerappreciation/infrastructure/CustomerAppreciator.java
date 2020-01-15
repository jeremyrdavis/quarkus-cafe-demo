package com.redhat.quarkus.customerappreciation.infrastructure;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.consumer.KafkaConsumerRecords;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CustomerAppreciator {

    @Inject
    Vertx vertx;

    private KafkaConsumer<String, String> kafkaConsumer;

    public String winner() {

        System.out.println("winner!");
        peek();
        return "Jeremy D.";
    }

    private void peek() {
        kafkaConsumer.subscribe("test", ar -> {

            if (ar.succeeded()) {
                System.out.println("Consumer subscribed");

                vertx.setPeriodic(1000, timerId -> {

                    kafkaConsumer.poll(100, ar1 -> {

                        if (ar1.succeeded()) {

                            KafkaConsumerRecords<String, String> records = ar1.result();
                            for (int i = 0; i < records.size(); i++) {
                                KafkaConsumerRecord<String, String> record = records.recordAt(i);
                                System.out.println("key=" + record.key() + ",value=" + record.value() +
                                        ",partition=" + record.partition() + ",offset=" + record.offset());
                            }
                        }
                    });

                });
            }
        });
    }

    @PostConstruct
    public void postConstruct() {
        // Config values can be moved to application.properties
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("acks", "1");
        kafkaConsumer = KafkaConsumer.create(vertx, config);
    }

}
