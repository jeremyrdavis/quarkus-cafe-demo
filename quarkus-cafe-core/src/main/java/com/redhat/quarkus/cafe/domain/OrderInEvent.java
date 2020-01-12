package com.redhat.quarkus.cafe.domain;

import java.util.UUID;

public abstract class OrderInEvent implements CafeEvent{

    public final String itemId = UUID.randomUUID().toString();
    public EventType eventType;
    public final String orderId;
    public String name;
    public Item item;

    public OrderInEvent(String orderId, String name, Item item, EventType eventType) {
        this.orderId = orderId;
        this.name = name;
        this.item = item;
        this.eventType = eventType;
    }

    public String getOrderId(){
        return orderId;
    }

    public Item getItem() {
        return item;
    }

    public EventType getEventType(){
        return eventType;
    }


}

