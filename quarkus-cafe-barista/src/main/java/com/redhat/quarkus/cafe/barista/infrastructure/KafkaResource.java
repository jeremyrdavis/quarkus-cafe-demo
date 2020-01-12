package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.OrderInEvent;
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

    @Incoming("ordersin")
    @Outgoing("ordersout")
    public CompletionStage<BeverageOrder> orderIn(String message) {

        logger.debug(message);

        System.out.println("order in:" + message);
        OrderInEvent orderInEvent = jsonb.fromJson(message, OrderInEvent.class);
        return barista.orderIn(orderInEvent);
    }

    public class KafkaTopics{

        public static final String INCOMING = "events";
        public static final String OUTGOING = "events";
    }


}
