package com.redhat.quarkus.cafe.domain;

public class BeverageOrderInEvent extends OrderEvent {

    public BeverageOrderInEvent(String orderId, String name, Item item) {
        super(EventType.BEVERAGE_ORDER_IN, orderId, name, item);
    }
}
