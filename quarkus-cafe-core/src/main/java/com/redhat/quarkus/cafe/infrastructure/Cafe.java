package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.createOrderCommandFromJson;
import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.toJson;

@ApplicationScoped
public class Cafe {

    final Logger logger = LoggerFactory.getLogger(Cafe.class);

    @Inject
    OrderRepository orderRepository;

    @Inject @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    @Inject @Channel("barista-out")
    Emitter<String> baristaOutEmitter;

    @Inject @Channel("kitchen-out")
    Emitter<String> kitchenOutEmitter;

    @Incoming("orders-in")
    public CompletionStage<Message> handleCreateOrderCommand(final Message message) {

        logger.debug("orderIn: {}", message.getPayload());

        Order order = OrderFactory.createFromCreateOrderCommand(createOrderCommandFromJson(message.getPayload().toString()));

        logger.debug("order created: {}", order);

        orderRepository.persist(order);
        OrderCreatedEvent orderCreatedEvent = EventFactory.createFromNewOrder(order);

        CompletableFuture<Void> broadcast = applyEvents(orderCreatedEvent.events);
        return message.ack();
    }

    private CompletableFuture<Void> applyEvents(List<LineItemEvent> events) {

        return CompletableFuture.supplyAsync(() ->{

            events.forEach(e -> {
                if (e.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                    baristaOutEmitter.send(toJson(e))
                            .thenAccept(s -> logger.debug("sent to barista-in topic {}", toJson(e)))
                            .exceptionally(ex -> {
                                logger.error(ex.getMessage());
                                throw new RuntimeException(ex.getMessage());
                            });
                } else if (e.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                    kitchenOutEmitter.send(toJson(e))
                            .thenAccept(s -> logger.debug("sent to kitchen-in topic {}", toJson(e)))
                            .exceptionally(ex -> {
                                logger.error(ex.getMessage());
                                throw new RuntimeException(ex.getMessage());
                            });
                }
            });
            return null;
        });
    }
}
