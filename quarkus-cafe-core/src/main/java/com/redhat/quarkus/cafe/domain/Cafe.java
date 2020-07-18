package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Cafe {

    static final Logger logger = LoggerFactory.getLogger(Cafe.class);

    @Inject
    OrderRepository orderRepository;

/*
    public OrderCreatedEvent processCreateOrderCommand(CreateOrderCommand createOrderCommand) {

        Order order = createOrderFromCommand(createOrderCommand);
        orderRepository.persist(order);

        // construct the OrderCreatedEvent
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.order = order;
        if (order.getBeverageLineItems().size() >= 1) {
            order.beverageLineItems.forEach(b -> {
                orderCreatedEvent.addEvent(new OrderInEvent(EventType.BEVERAGE_ORDER_IN, order.id.toString(), b.name, b.item));
            });
        }
        if (order.getKitchenLineItems().size() >= 1) {
            order.kitchenLineItems.forEach(k -> {
                orderCreatedEvent.addEvent(new OrderInEvent(EventType.KITCHEN_ORDER_IN, order.id.toString(), k.name, k.item));
            });
        }
        logger.debug("createEventFromCommand: returning OrderCreatedEvent {}", orderCreatedEvent.toString());
        return orderCreatedEvent;
    }
*/

}
