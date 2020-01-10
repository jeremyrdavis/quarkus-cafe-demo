package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.Kitchen;
import com.redhat.quarkus.cafe.kitchen.domain.KitchenOrder;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EventUpdateService {

    Logger logger = Logger.getLogger(EventUpdateService.class);

    @Inject
    Kitchen kitchen;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("coffee-orders-in")
    @Outgoing("coffee-orders-out")
    public CompletionStage<KitchenOrder> orderIn(String message) {

        logger.debug(message);
        System.out.println("order in:" + message);
        KitchenOrder beverageOrder = jsonb.fromJson(message, KitchenOrder.class);
        logger.debug(beverageOrder.toString());
        return kitchen.orderIn(beverageOrder);
    }

}
