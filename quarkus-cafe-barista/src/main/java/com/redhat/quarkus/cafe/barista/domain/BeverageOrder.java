package com.redhat.quarkus.cafe.barista.domain;

public abstract class BeverageOrder {

    public EventType eventType;
    public String itemId;
    public String orderId;
    public String name;
    public Item item;

    public BeverageOrder() {
    }

    public BeverageOrder(String orderId, String itemId, String name, Item item) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("BeverageOrder[")
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
