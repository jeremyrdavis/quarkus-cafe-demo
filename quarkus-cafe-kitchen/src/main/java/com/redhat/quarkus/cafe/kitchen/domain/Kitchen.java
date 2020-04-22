package com.redhat.quarkus.cafe.kitchen.domain;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Kitchen {

    static final Logger logger = LoggerFactory.getLogger(Kitchen.class.getName());

    @Inject
    @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    private String madeBy;

    @PostConstruct
    void setHostName() {
        try {
            madeBy = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            madeBy = "unknown";
        }
    }

    @Incoming("orders-in")
    public CompletionStage<Void> handleOrderIn(Message message) {

        logger.debug("\nKitchen Order In Received: {}", message.getPayload());
        final OrderIn orderIn = jsonb.fromJson((String) message.getPayload(), OrderIn.class);
        if (orderIn.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
            processOrderIn(orderIn).toCompletableFuture();
        }
        return message.ack();
    }

    public CompletionStage<Void> processOrderIn(final OrderIn orderIn) {

        logger.debug("orderIn: " + orderIn.toString());
        return CompletableFuture.supplyAsync(() -> {
            switch (orderIn.item) {
                case CAKEPOP:
                    return prepare(orderIn, 3);
                case COOKIE:
                    return prepare(orderIn, 3);
                case MUFFIN:
                    return prepare(orderIn, 3);
                case PANINI:
                    return prepare(orderIn, 10);
                default:
                    return prepare(orderIn, 5);

            }
        }).thenAccept(b -> {
            logger.debug("returning: {}", b);
            orderUpEmitter.send(jsonb.toJson(b));
        });
    }

    private OrderEvent prepare(final OrderIn orderIn, int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        OrderEvent retVal = new OrderUp(orderIn, madeBy);
        return retVal;
    }

}
