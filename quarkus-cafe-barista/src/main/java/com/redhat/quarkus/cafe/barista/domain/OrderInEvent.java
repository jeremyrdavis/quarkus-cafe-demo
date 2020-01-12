package com.redhat.quarkus.cafe.barista.domain;

public class OrderInEvent {

    public EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public String orderId;

    public String itemId;

    public String name;

    public Item item;

    public OrderInEvent(String orderId, String itemId, String name, Item item) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
    }

    public OrderInEvent() {
    }
}
