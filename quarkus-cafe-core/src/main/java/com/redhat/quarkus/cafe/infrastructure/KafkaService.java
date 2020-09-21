package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.core.domain.Order;
import com.redhat.quarkus.cafe.core.domain.OrderCreatedEvent;
import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.LineItemEvent;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.reactive.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.*;

@ApplicationScoped @RegisterForReflection
public class KafkaService {

    final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    OrderRepository orderRepository;

    @Inject
    @Channel("barista-out")
    Emitter<String> baristaOutEmitter;

    @Inject
    @Channel("kitchen-out")
    Emitter<String> kitchenOutEmitter;

    @Inject
    @Channel("web-updates-out")
    Emitter<String> webUpdatesOutEmitter;

    @Incoming("web-in")
    public CompletionStage<Void> onOrderIn(final Message message) {
        logger.debug("orderIn: {}", message.getPayload());
        return handleCreateOrderCommand(createOrderCommandFromJson(message.getPayload().toString())).thenRun(()->{message.ack();});
    }

    CompletableFuture<Void> sendBaristaOrder(final LineItemEvent event) {
        return baristaOutEmitter.send(toJson(event)).thenRun(() ->{
            sendWebUpdate(event);
        }).toCompletableFuture().toCompletableFuture();
    }

    CompletableFuture<Void> sendKitchenOrder(final LineItemEvent event) {
        return kitchenOutEmitter.send(toJson(event)).thenRun(() ->{
            sendWebUpdate(event);
        }).toCompletableFuture();
    }

    CompletableFuture<Void> sendWebUpdate(final LineItemEvent event) {
        return webUpdatesOutEmitter.send(toInProgressUpdate(event)).toCompletableFuture();
    }

    protected CompletionStage<Void> handleCreateOrderCommand(final OrderInCommand orderInCommand) {

        // Get the event from the Order domain object
        OrderCreatedEvent orderCreatedEvent = Order.processCreateOrderCommand(orderInCommand);
        orderRepository.persist(orderCreatedEvent.order);

        Collection<CompletableFuture<Void>> futures = new ArrayList<>(orderCreatedEvent.getEvents().size() * 2);
        orderCreatedEvent.getEvents().forEach(e ->{
            if (e.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                futures.add(sendBaristaOrder(e));
            } else if (e.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                futures.add(sendKitchenOrder(e));
            }
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .exceptionally(e -> {
                    logger.error(e.getMessage());
                    return null;
                });
    }

    @Incoming("orders-up")
    @Outgoing("web-updates-order-up")
    public String onOrderUp(String payload) {
        logger.debug("received order up {}", payload);
        return toDashboardUpdateReadyJson(payload);
    }
}