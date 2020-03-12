package com.redhat.quarkus.customerappreciation.infrastructure;

import com.redhat.quarkus.customerappreciation.domain.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.*;

@ApplicationScoped
public class CustomerAppreciator {

    Logger logger = LoggerFactory.getLogger(CustomerAppreciator.class);

    @Inject @Channel("customerappreciation-outgoing")
    Emitter<String> customerAppreciationEventEmiiter;

    Jsonb jsonb = JsonbBuilder.create();

    private HashMap<String, Customer> customers = new HashMap<>();
/*

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
*/

    /**
     * Returns a randomly selected winner.  Customers are not eligible to win twice.
     *
     * @return Customer
     */
    public Customer pickWinner() throws NoEligibleCustomersException {

        if (customers.isEmpty()) {
            throw new NoEligibleCustomersException();
        }
            System.out.println("\npicking winner\n");

            List<String> customerNames = new ArrayList(customers.keySet());
            Collections.shuffle(customerNames);
            String winningName = customerNames.get(new Random().nextInt(customerNames.size()));
            Customer winner = customers.get(winningName);
            logger.debug("And the winner is {}", winner.name);
            customers.remove(winner.name);
            winner.customerStatus = CustomerStatus.WINNER;
            customers.put(winner.name, winner);

            logger.debug("Returning {}", winner);
            return winner;
/*
            System.out.println("\nwinner: " + winner + "\n");
            String message = jsonb.toJson(new CustomerAppreciationEvent(winner.name));
            customerAppreciationEventEmiiter.send(message);
*/
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    @Incoming("add-customer")
    public void addCustomer(String customerName) {
        System.out.println("adding customer: " + customerName);
        customers.entrySet().forEach(c -> System.out.println(c));
        logger.debug("adding customer {}", customerName);
        this.customers.put(customerName, new Customer(customerName, CustomerStatus.ENTERED));
    }
}
