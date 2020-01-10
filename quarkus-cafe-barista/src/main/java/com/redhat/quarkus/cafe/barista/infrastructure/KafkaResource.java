package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaResource {

    Logger logger = Logger.getLogger(KafkaResource.class);

    @Inject
    Barista barista;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("coffee-orders-in")
    @Outgoing("coffee-orders-out")
    public CompletionStage<BeverageOrder> orderIn(String message) {

        logger.debug(message);
        System.out.println("order in:" + message);
        BeverageOrder beverageOrder = jsonb.fromJson(message, BeverageOrder.class);
        logger.debug(beverageOrder.toString());
        return barista.orderIn(beverageOrder);
    }

    public class KafkaTopics{

        public static final String INCOMING = "events";
        public static final String OUTGOING = "events";
    }


}
