package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import com.redhat.quarkus.cafe.domain.OrderUpEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class Barista {

    static final Logger logger = LoggerFactory.getLogger(Barista.class);

    private String madeBy = "undefined";

    @PostConstruct
    void setHostName() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            if (hostName == null || hostName.length() <= 0) {
                madeBy = "default";
            }
        } catch (IOException e) {
            logger.info("unable to get hostname; using default");
            madeBy = "unknown";
        }
    }

    public CompletableFuture<OrderUpEvent> make(final OrderInEvent orderInEvent) {

        logger.debug("orderIn: " + orderInEvent.toString());
        return CompletableFuture.supplyAsync(() -> {

            switch(orderInEvent.item){
                case COFFEE_BLACK:
                    return prepare(orderInEvent, 5);
                case COFFEE_WITH_ROOM:
                    return prepare(orderInEvent, 5);
                case ESPRESSO:
                    return prepare(orderInEvent, 7);
                case ESPRESSO_DOUBLE:
                    return prepare(orderInEvent, 7);
                case CAPPUCCINO:
                    return prepare(orderInEvent, 9);
                default:
                    return prepare(orderInEvent, 11);
            }
        });
    }

    private OrderUpEvent prepare(final OrderInEvent orderInEvent, int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new OrderUpEvent(
                EventType.BEVERAGE_ORDER_UP,
                orderInEvent.orderId,
                orderInEvent.name,
                orderInEvent.item,
                madeBy);
    }

}
