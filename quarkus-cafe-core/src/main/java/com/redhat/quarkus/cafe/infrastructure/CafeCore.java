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
import java.util.List;
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

    @Inject @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    @Inject @Channel("barista-out")
    Emitter<String> baristaOutEmitter;

    @Inject @Channel("kitchen-out")
    Emitter<String> kitchenOutEmitter;

    @Incoming("orders-in")
    public CompletionStage<Message> ordersIn(final Message message) {

        logger.debug("orderIn: {}", message.getPayload());
        List<OrderEvent> allEvents = cafe.orderIn(createOrderCommandFromJson(message.getPayload().toString()));
        allEvents.forEach(orderEvent -> {
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                baristaOutEmitter.send(toJson(orderEvent));
                logger.debug("sent to barista-orders topic: {}", toJson(orderEvent));
/*
                Uni.createFrom()
                        .completionStage(baristaOutEmitter.send(toJson(orderEvent)))
                        .subscribe()
                        .with(  v -> logger.debug("sent {}", orderEvent),
                                failure -> logger.error(failure.getMessage()));
*/
            } else if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                kitchenOutEmitter.send(toJson(orderEvent));
                logger.debug("sent to kitchen-orders topic: {}", toJson(orderEvent));

/*
                Uni.createFrom()
                        .completionStage(kitchenOutEmitter.send(toJson(orderEvent)))
                        .subscribe()
                        .with(  v -> logger.debug("sent {}", orderEvent),
                                failure -> logger.error(failure.getMessage()));
*/
            }
        });
        return message.ack();
    }

/*
    public CompletableFuture<Void> sendOrders(final Message message) {

        CompletableFuture<List<OrderEvent>> completableFuture = CompletableFuture.supplyAsync(() -> {
                    return cafe.orderIn(createOrderCommandFromJson(message.getPayload().toString()));
                });

        CompletableFuture<Void> future = completableFuture.thenAccept(orderEvents -> {
            orderEvents.forEach(o -> System.out.print(o));
        });

        return future.get();
    }
*/


    private void sendToKafka(final OrderEvent orderEvent) {
        Uni.createFrom()
                .completionStage(baristaOutEmitter.send(toJson(orderEvent)))
                .subscribe()
                .with(  v -> logger.debug("sent {}", orderEvent),
                        failure -> logger.error(failure.getMessage()));

    }

    private void sendEvents(final List<OrderEvent> orderEvents) {

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
    public List<OrderEvent> orderIn(final CreateOrderCommand createOrderCommand) throws ExecutionException, InterruptedException {

        List<OrderEvent> allEvents = cafe.orderIn(createOrderCommand);
        sendEvents(allEvents);
        return allEvents;
    }

    private List<DashboardUpdate> convertJson(List<OrderEvent> orderEvents) {
        return orderEvents.stream()
                .map(orderEvent -> {
            System.out.println("\nConverting: " + orderEvent.toString() +"\n");
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
