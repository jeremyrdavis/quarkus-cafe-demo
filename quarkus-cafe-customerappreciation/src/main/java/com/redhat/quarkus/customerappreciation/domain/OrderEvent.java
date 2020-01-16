package com.redhat.quarkus.customerappreciation.domain;

public class OrderEvent {

    public String itemId;
    public String orderId;
    public String eventType;
    public String name;
    public String item;

    public OrderEvent() {
    }

    public OrderEvent(String itemId, String orderId, String eventType, String name, String item) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.name = name;
        this.item = item;
    }
}
