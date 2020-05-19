package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderUp extends OrderEvent{

    public String madeBy;

    public EventType eventType = EventType.KITCHEN_ORDER_UP;

    public OrderUp(OrderIn orderIn, String madeBy) {
        this.itemId = orderIn.itemId;
        this.orderId = orderIn.orderId;
        this.name = orderIn.name;
        this.item = orderIn.item;
        this.madeBy = madeBy;
    }

    public OrderUp(String itemId, String orderId, String name, Item item, String madeBy) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
        this.madeBy = madeBy;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("OrderUp[")
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
                .append(",madeBy=")
                .append(madeBy)
                .append("]")
                .toString();
    }
}
