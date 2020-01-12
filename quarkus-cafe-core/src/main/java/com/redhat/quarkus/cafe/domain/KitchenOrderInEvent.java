package com.redhat.quarkus.cafe.domain;

public class KitchenOrderInEvent extends OrderInEvent {

    public KitchenOrderInEvent(String orderId, String name, Item item) {

        super(orderId, name, item, EventType.KITCHEN_ORDER_IN);
    }

}
