package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.UUID;

/**
 * This class represents all orders in the system
 */
@RegisterForReflection
public class OrderEvent {

    public String itemId;
    public EventType eventType;
    public String orderId;
    public String name;
    public Item item;

    public OrderEvent(String orderId, String name, Item item, String itemId, EventType eventType) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
        this.itemId = itemId;
    }

    public OrderEvent() {
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

    @Override
    public String toString() {
        return new StringBuilder().append("OrderEvent[orderId=")
                .append(orderId)
                .append(",eventType=")
                .append(eventType.toString())
                .append(",item=")
                .append(item)
                .append(",name=")
                .append(name)
                .append(",itemId=")
                .append(itemId)
                .append("]")
                .toString();
    }
}
