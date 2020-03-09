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

    private HashMap<String, Customer> customers = new HashMap<>();

    @Incoming("ordersin")
    public void winner(String payload) {

        try {
            OrderEvent orderEvent = jsonb.fromJson(payload, OrderEvent.class);
            customers.put(orderEvent.name, new Customer(orderEvent.name, CustomerStatus.ENTERED));
            System.out.println("\nadding customer\n");
            System.out.println(payload);
            System.out.println("\nnow with " + customers.size() + " customers\n");
        } catch (Exception e) {
            System.out.println("Error parsing: " + payload);
        }
    }

    @Scheduled(every="15s")
    public void pickWinner() {

        if (customers.size() >= 1) {

            System.out.println("\npicking winner\n");

            List<Customer> customerNames = new ArrayList(customers.keySet());
            Collections.shuffle(customerNames);
            Customer winner = customerNames.get(new Random().nextInt(customerNames.size()));
            customers.remove(winner.name);
            winner.customerStatus = CustomerStatus.WINNER;
            customers.put(winner.name, winner);

            System.out.println("\nwinner: " + winner + "\n");
            String message = jsonb.toJson(new CustomerAppreciationEvent(winner.name));
            customerAppreciationEventEmiiter.send(message);
        }
    }
}
