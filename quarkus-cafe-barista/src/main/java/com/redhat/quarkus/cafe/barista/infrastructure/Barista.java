package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.EventType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Barista {

    Logger logger = LoggerFactory.getLogger(Barista.class);

    @Inject @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> orderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());

        JsonReader reader = Json.createReader(new StringReader((String) message.getPayload()));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        if (eventType.equals(EventType.BEVERAGE_ORDER_IN.toString())) {
            final BeverageOrder beverageOrder = jsonb.fromJson((String) message.getPayload(), BeverageOrder.class);
            orderIn(beverageOrder).toCompletableFuture();
        }
        return message.ack();
    }

    public CompletionStage<Void> orderIn(BeverageOrder beverageOrder) {

        logger.debug("orderIn: " + beverageOrder.toString());

        return CompletableFuture.supplyAsync(() -> {

            switch(beverageOrder.item){
                case COFFEE_BLACK:
                    return prepare(beverageOrder, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(beverageOrder, 5);
                case ESPRESSO:
                    return prepare(beverageOrder, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(beverageOrder, 7);
                case LATTE:
                    return prepare(beverageOrder, 7);
                case CAPPUCCINO:
                    return prepare(beverageOrder, 9);
                default:
                    return prepare(beverageOrder, 11);
            }
        }).thenAccept(b -> {
            logger.debug("returning: {}", b);
            orderUpEmitter.send(jsonb.toJson(b));
        });
    }

    private BeverageOrder prepare(final BeverageOrder beverageOrder, int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        BeverageOrder retVal = new BeverageOrder(EventType.BEVERAGE_ORDER_UP, beverageOrder.orderId, beverageOrder.itemId, beverageOrder.name, beverageOrder.item);
        System.out.println("returning: " + retVal.toString());
        return retVal;
    }

}
