package com.redhat.quarkus.cafe.domain;

public class EventFactory {

    /**
     * Create an OrderCreatedEvent containing LineItemInEvents
     *
     * @param order
     * @return
     */
    public static OrderCreatedEvent createFromNewOrder(Order order) {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        if (order.beverageLineItems != null) {
            order.beverageLineItems.forEach(b -> {
                orderCreatedEvent.addEvent(new BeverageLineItemInEvent(order.id.toString(), b.name, b.item));
            });
        }
        if (order.kitchenLineItems != null) {
            order.kitchenLineItems.forEach(k -> {
                orderCreatedEvent.addEvent(new KitchenLineItemInEvent(order.id.toString(), k.name, k.item));
            });
        }
        return orderCreatedEvent;
    }
}
