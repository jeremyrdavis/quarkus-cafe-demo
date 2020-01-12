package com.redhat.quarkus.cafe.domain;

public class BeverageOrderInEvent extends OrderInEvent {

    public BeverageOrderInEvent(String orderId, String name, Item item) {
        super(orderId, name, item, EventType.BEVERAGE_ORDER_IN);
    }
}
