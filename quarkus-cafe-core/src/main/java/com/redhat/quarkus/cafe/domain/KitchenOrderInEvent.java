package com.redhat.quarkus.cafe.domain;

public class KitchenOrderInEvent extends OrderInEvent {

    public final EventType eventType = EventType.KITCHEN_ORDER_IN;

    public KitchenOrderInEvent(String orderId, String name, Item item) {
        super(orderId, name, item);
    }

}
