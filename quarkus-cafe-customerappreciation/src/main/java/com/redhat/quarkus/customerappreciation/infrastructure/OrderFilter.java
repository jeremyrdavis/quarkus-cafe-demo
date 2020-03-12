package com.redhat.quarkus.customerappreciation.infrastructure;

import com.redhat.quarkus.customerappreciation.domain.OrderEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.StringReader;

@ApplicationScoped
public class OrderFilter {

    Logger logger = LoggerFactory.getLogger(OrderFilter.class);

    Jsonb jsonb = JsonbBuilder.create();

    @Inject
    CustomerAppreciator customerAppreciator;

    @Incoming("ordersin")
    @Outgoing("add-customer")
    public String filter(String payload) {

        System.out.println(payload);
        logger.debug("order received {}", payload);
        OrderEvent orderEvent = jsonb.fromJson(payload, OrderEvent.class);
//        customerAppreciator.addCustomer(orderEvent.name);
        return orderEvent.name;
    }

}
