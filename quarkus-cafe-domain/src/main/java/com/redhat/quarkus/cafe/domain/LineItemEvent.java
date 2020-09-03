package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

@RegisterForReflection
public abstract class LineItemEvent implements Event{

    public String itemId;
    public String orderId;
    public EventType eventType;
    public String name;
    public Item item;

    public LineItemEvent() {
        super();
    }

    public LineItemEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public LineItemEvent(EventType eventType, String orderId, String name, Item item, String itemId) {
        this.itemId = itemId;
        this.eventType = eventType;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }

    public LineItemEvent(EventType eventType, String orderId, String name, Item item) {
        this.itemId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }
}
