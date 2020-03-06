package com.redhat.quarkus.cafe.web.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderEvent {

    public String itemId;
    public String orderId;
    public EventType eventType;
    public String name;
    public Item item;

    public OrderEvent() {
    }

    public OrderEvent(String itemId, String orderId, EventType eventType, String name, Item item) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.name = name;
        this.item = item;
    }
}
