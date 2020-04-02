package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    Jsonb jsonb = JsonbBuilder.create();

    /**
     *
     * @param createOrderCommand
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<OrderEvent> orderIn(CreateOrderCommand createOrderCommand) throws ExecutionException, InterruptedException {

        List<OrderEvent> allEvents = cafe.orderIn(createOrderCommand);

        try {
            allEvents.forEach(orderEvent -> {
                if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                    logger.debug("sending to barista-orders topic: {}", orderEvent);
                    baristaOutEmitter.send(toJson(orderEvent));
                } else if (orderEvent.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                    logger.debug("sending to kitchen-orders topic: {}", orderEvent);
                    kitchenOutEmitter.send(toJson(orderEvent));
                }
                logger.debug("completed sending: " + orderEvent);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
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
