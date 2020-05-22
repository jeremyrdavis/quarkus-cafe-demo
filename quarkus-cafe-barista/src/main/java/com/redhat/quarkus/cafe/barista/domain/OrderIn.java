package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderIn extends OrderEvent{

    public EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public OrderIn() {
    }

    public OrderIn(String itemId, String orderId, String name, Item item) {
        super(itemId, orderId, name, item);
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
