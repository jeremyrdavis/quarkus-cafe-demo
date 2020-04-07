package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.createOrderCommandFromJson;
import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.toJson;

@ApplicationScoped
public class CafeCore {

    Logger logger = LoggerFactory.getLogger(CafeCore.class);

    @Inject
    Cafe cafe;

    @Inject
    OrderRepository orderRepository;

    @Inject @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    @Inject @Channel("barista-out")
    Emitter<String> baristaOutEmitter;

    @Inject @Channel("kitchen-out")
    Emitter<String> kitchenOutEmitter;

    @Incoming("orders-in") @Transactional
    public CompletionStage<Message> handleCreateOrderCommand(final Message message) {

        logger.debug("orderIn: {}", message.getPayload());
        Order order = OrderFactory.createFromCreateOrderCommand(createOrderCommandFromJson(message.getPayload().toString()));
        logger.debug("order created: {}", order);
        order.persist();
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


/*
    @Incoming("orders-in")
    public CompletionStage<Message> ordersIn(final Message message) {

        logger.debug("orderIn: {}", message.getPayload());
        List<OrderEvent> allEvents = cafe.orderIn(createOrderCommandFromJson(message.getPayload().toString()));
        allEvents.forEach(orderEvent -> {
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                baristaOutEmitter.send(toJson(orderEvent));
                logger.debug("sent to barista-orders topic: {}", toJson(orderEvent));
*/
/*
                Uni.createFrom()
                        .completionStage(baristaOutEmitter.send(toJson(orderEvent)))
                        .subscribe()
                        .with(  v -> logger.debug("sent {}", orderEvent),
                                failure -> logger.error(failure.getMessage()));
*//*

            } else if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                kitchenOutEmitter.send(toJson(orderEvent));
                logger.debug("sent to kitchen-orders topic: {}", toJson(orderEvent));

*/
/*
                Uni.createFrom()
                        .completionStage(kitchenOutEmitter.send(toJson(orderEvent)))
                        .subscribe()
                        .with(  v -> logger.debug("sent {}", orderEvent),
                                failure -> logger.error(failure.getMessage()));
*//*

            }
        });
        return message.ack();
    }
*/

    private void sendToKafka(final LineItemEvent orderEvent) {
        Uni.createFrom()
                .completionStage(baristaOutEmitter.send(toJson(orderEvent)))
                .subscribe()
                .with(  v -> logger.debug("sent {}", orderEvent),
                        failure -> logger.error(failure.getMessage()));

    }

    private void sendEvents(final List<LineItemEvent> orderEvents) {

        try {
            orderEvents.forEach(orderEvent -> {
                if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                    baristaOutEmitter.send(toJson(orderEvent));
                    logger.debug("sent to barista-orders topic: {}", toJson(orderEvent));
                } else if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                    kitchenOutEmitter.send(toJson(orderEvent));
                    logger.debug("sent to kitchen-orders topic: {}", toJson(orderEvent));
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param createOrderCommand
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<LineItemEvent> orderIn(final CreateOrderCommand createOrderCommand) throws ExecutionException, InterruptedException {

        List<LineItemEvent> allEvents = cafe.orderIn(createOrderCommand);
        sendEvents(allEvents);
        return allEvents;
    }

    private List<DashboardUpdate> convertJson(List<LineItemEvent> orderEvents) {
        return orderEvents.stream()
                .map(orderEvent -> {
            logger.debug("\nConverting: " + orderEvent.toString() +"\n");

            OrderStatus status;
            switch(orderEvent.eventType){
                case BEVERAGE_ORDER_IN:
                    status = OrderStatus.IN_QUEUE;
                    break;
                case BEVERAGE_ORDER_UP:
                    status = OrderStatus.READY;
                    break;
                case KITCHEN_ORDER_IN:
                    status = OrderStatus.IN_QUEUE;
                    break;
                case KITCHEN_ORDER_UP:
                    status = OrderStatus.READY;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown status" + orderEvent.eventType);
            }
            return new DashboardUpdate(
                    orderEvent.orderId,
                    orderEvent.itemId,
                    orderEvent.name,
                    orderEvent.item,
                    status);
        }).collect(Collectors.toList());
    }

}
