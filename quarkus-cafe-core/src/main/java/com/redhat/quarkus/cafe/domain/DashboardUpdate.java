package com.redhat.quarkus.cafe.domain;

public class DashboardUpdate {

    public String orderId;

    public String itemId;

    public String name;

    public Item item;

    public OrderStatus status;

    public DashboardUpdate() {
    }

    public DashboardUpdate(String orderId, String itemId, String name, Item item, OrderStatus status) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
    }
}
