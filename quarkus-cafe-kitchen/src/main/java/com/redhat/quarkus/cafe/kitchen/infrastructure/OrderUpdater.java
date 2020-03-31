package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import org.eclipse.microprofile.reactive.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class OrderUpdater {

    Logger logger = LoggerFactory.getLogger(OrderUpdater.class.getName());

    Jsonb jsonb = JsonbBuilder.create();

    @Inject @Channel("orders-out")
    Emitter<String> emitter;

    public void updateOrder(final OrderEvent orderEvent) {

        logger.debug("updating order: {}", orderEvent);
        emitter.send(jsonb.toJson(orderEvent));
    }

}
