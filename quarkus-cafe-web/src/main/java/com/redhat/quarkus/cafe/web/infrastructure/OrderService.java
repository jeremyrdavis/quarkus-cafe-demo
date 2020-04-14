package com.redhat.quarkus.cafe.web.infrastructure;


import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.convertOrderEventToDashboardUpdate;
import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@RegisterForReflection
@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    public void placeOrder(CreateOrderCommand createOrderCommand){

        ordersOutEmitter.send(toJson(createOrderCommand))
            .whenCompleteAsync((result, ex) -> {
                logger.debug("createOrderCommand sent");
                logger.debug(ex.getMessage());
            });

    }

    @Incoming("barista-in")
    @Outgoing("web-updates-out")
    public PublisherBuilder<String> onBeverageOrderIn(PublisherBuilder<String> payload) {

        logger.debug("Barista event received {}", payload);
        return payload.map(order -> convertOrderEventToDashboardUpdate(order));

    }

    @Incoming("kitchen-in")
    @Outgoing("web-updates-out")
    public PublisherBuilder<String> onKitchenOrderIn(PublisherBuilder<String> payload) {

        logger.debug("Kitchen event received {}", payload);
        return payload.map(order -> convertOrderEventToDashboardUpdate(order));
    }

    @Incoming("orders")
    @Outgoing("web-updates-out")
    public PublisherBuilder<String> onOrderUp(PublisherBuilder<String> payload) {

        logger.debug("OrderUpEvent received {}", payload);
        return payload.map(order -> convertOrderEventToDashboardUpdate(order));
    }
}
