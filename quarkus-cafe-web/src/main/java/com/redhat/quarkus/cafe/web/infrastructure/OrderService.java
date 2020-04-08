package com.redhat.quarkus.cafe.web.infrastructure;


import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.convertOrderEventToDashboardUpdate;
import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    public void placeOrder(CreateOrderCommand createOrderCommand){

        ordersOutEmitter.send(toJson(createOrderCommand));
    }

    @Incoming("barista-in")
    @Outgoing("web-updates-out")
    public String onBeverageOrderIn(final String payload) {

        logger.debug("Barista event received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);

    }

    @Incoming("kitchen-in")
    @Outgoing("web-updates-out")
    public String onKitchenOrderIn(final String payload) {

        logger.debug("Kitchen event received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);
    }

    @Incoming("orders")
    @Outgoing("web-updates-out")
    public String onOrderUp(final String payload) {

        logger.debug("OrderUpEvent received {}", payload);
        return convertOrderEventToDashboardUpdate(payload);
    }
}
