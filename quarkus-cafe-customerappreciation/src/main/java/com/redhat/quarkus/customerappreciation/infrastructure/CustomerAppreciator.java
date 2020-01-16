package com.redhat.quarkus.customerappreciation.infrastructure;

import com.redhat.quarkus.customerappreciation.domain.Customer;
import com.redhat.quarkus.customerappreciation.domain.CustomerAppreciationEvent;
import com.redhat.quarkus.customerappreciation.domain.CustomerStatus;
import com.redhat.quarkus.customerappreciation.domain.OrderEvent;
import io.netty.util.concurrent.OrderedEventExecutor;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.consumer.KafkaConsumerRecords;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;

@ApplicationScoped
public class CustomerAppreciator {

    @Inject @Channel("ordersout")
    private Emitter<String> customerAppreciationEventEmiiter;

    Jsonb jsonb = JsonbBuilder.create();

    private HashMap<String, CustomerStatus> customers = new HashMap<>();

/*
    @Inject
    Vertx vertx;

    private KafkaConsumer<String, String> kafkaConsumer;
*/

    @Incoming("ordersin")
    public void winner(String payload) {

        try {
            OrderEvent orderEvent = jsonb.fromJson(payload, OrderEvent.class);
            customers.put(orderEvent.name, CustomerStatus.ENTERED);
            System.out.println("\nadding customer\n");
            System.out.println(payload);
            System.out.println("\nnow with " + customers.size() + " customers\n");
        } catch (Exception e) {
            System.out.println("Error parsing: " + payload);
        }
    }

//    @Scheduled(every="15s")
    public void pickWinner() {

        if (customers.size() >= 1) {

            System.out.println("\npicking winner\n");

            List<Customer> customerNames = new ArrayList(customers.keySet());
            Collections.shuffle(customerNames);
            Customer winner = customerNames.get(new Random().nextInt(customerNames.size()));
            customers.remove(winner.name);

            System.out.println("\nwinner: " + winner + "\n");
            String message = jsonb.toJson(new CustomerAppreciationEvent(winner.name));
            customerAppreciationEventEmiiter.send(message);
        }
    }



/*
    public String winner() {

        System.out.println("winner!");
        peek();
        return "Jeremy D.";
    }
*/

/*
    private void peek() {
        kafkaConsumer.subscribe("orders", ar -> {

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
*/

/*
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
*/

}
