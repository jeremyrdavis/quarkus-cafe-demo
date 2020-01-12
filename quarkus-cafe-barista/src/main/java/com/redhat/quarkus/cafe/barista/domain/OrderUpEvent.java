package com.redhat.quarkus.cafe.barista.domain;

public class OrderUpEvent extends BeverageOrder{

    public final EventType eventType = EventType.BEVERAGE_ORDER_UP;

    public OrderUpEvent(String orderId, String itemId, String name, Item item) {
        super(orderId, itemId, name, item);
    }
}
