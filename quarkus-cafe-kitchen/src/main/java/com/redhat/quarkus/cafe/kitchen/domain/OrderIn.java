package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderIn extends OrderEvent{

    public EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public OrderIn() {
    }

    public OrderIn(String orderId, String name, Item item, String itemId) {
        super(orderId, name, item, itemId);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("OrderIn[")
                .append("name=")
                .append(name)
                .append(",item=")
                .append(item)
                .append(",itemId=")
                .append(itemId)
                .append(",eventType=")
                .append(eventType)
                .append(",orderId=")
                .append(orderId)
                .append("]")
                .toString();
    }

}
