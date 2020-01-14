package com.redhat.quarkus.cafe.domain;

public class KitchenOrderInEvent extends OrderEvent {

    public KitchenOrderInEvent(String orderId, String name, Item item) {
        super(EventType.KITCHEN_ORDER_IN, orderId, name, item);
    }

}
