package com.redhat.quarkus.cafe.web.domain;

public class DashboardUpdate {

    public String orderId;

    public String itemId;

    public String name;

    public Item item;

    public OrderStatus status;

    public DashboardUpdate(String orderId, String itemId, String name, Item item, OrderStatus status) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
    }

    public DashboardUpdate() {
    }

    @Override
    public String toString() {
        return new StringBuilder().append("DashboardUpdate[")
                .append("orderId=")
                .append(orderId)
                .append(",itemId=")
                .append(itemId)
                .append(",name=")
                .append(name)
                .append(",item=")
                .append(item)
                .append(",status=")
                .append(status)
                .append("]").toString();

    }
}
