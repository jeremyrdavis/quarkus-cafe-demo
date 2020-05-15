package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.OrderEvent;
import com.redhat.quarkus.cafe.barista.domain.EventType;
import com.redhat.quarkus.cafe.barista.domain.OrderIn;
import com.redhat.quarkus.cafe.barista.domain.OrderUp;
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
public class Barista {

    Logger logger = LoggerFactory.getLogger(Barista.class);

    String hostName;

    @Inject @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    @PostConstruct
    void setHostName() {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            hostName = "unknown";
        }
    }

    @Incoming("orders-in")
    public CompletionStage<Void> handleOrderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());
        final OrderIn orderIn = jsonb.fromJson((String) message.getPayload(), OrderIn.class);
        if (orderIn.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
            return processOrderIn2(orderIn).thenApply(o -> {
                return orderUpEmitter.send(jsonb.toJson(o));
            }).thenRun( () -> { message.ack(); });
        }else{
            return message.ack();
        }
    }

    public CompletableFuture<OrderUp> processOrderIn2(OrderIn orderIn) {

        logger.debug("orderIn: " + orderIn.toString());
        return CompletableFuture.supplyAsync(() -> {

            switch(orderIn.item){
                case COFFEE_BLACK:
                    return prepare(orderIn, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(orderIn, 5);
                case ESPRESSO:
                    return prepare(orderIn, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(orderIn, 7);
                case LATTE:
                    return prepare(orderIn, 7);
                case CAPPUCCINO:
                    return prepare(orderIn, 9);
                default:
                    return prepare(orderIn, 11);
            }
        });
    }


    public CompletionStage<Void> processOrderIn(final OrderIn orderIn) {

        logger.debug("orderIn: " + orderIn.toString());
        return CompletableFuture.supplyAsync(() -> {

            switch(orderIn.item){
                case COFFEE_BLACK:
                    return prepare(orderIn, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(orderIn, 5);
                case ESPRESSO:
                    return prepare(orderIn, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(orderIn, 7);
                case LATTE:
                    return prepare(orderIn, 7);
                case CAPPUCCINO:
                    return prepare(orderIn, 9);
                default:
                    return prepare(orderIn, 11);
            }
        }).thenAccept(b -> {
            logger.debug("returning: {}", b);
            orderUpEmitter.send(jsonb.toJson(b));
        });
    }

    private OrderUp prepare(final OrderIn orderIn, int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        OrderUp retVal = new OrderUp(orderIn, hostName);
        return retVal;
    }

}
