package com.redhat.quarkus.cafe.domain;

public class BeverageOrderInEvent extends OrderInEvent {

    public final EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public BeverageOrderInEvent(String orderId, String name, Item item) {
        super(orderId, name, item);
    }
}
