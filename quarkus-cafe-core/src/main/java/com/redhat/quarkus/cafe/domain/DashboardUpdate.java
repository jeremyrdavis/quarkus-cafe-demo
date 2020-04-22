package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
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

    public DashboardUpdate(LineItemEvent lineItemEvent) {
        this.orderId = lineItemEvent.orderId;
        this.itemId = lineItemEvent.itemId;
        this.name = lineItemEvent.name;
        this.item = lineItemEvent.item;
        switch (lineItemEvent.eventType) {
            case BEVERAGE_ORDER_IN:
                this.status = OrderStatus.IN_QUEUE;
                break;
            case KITCHEN_ORDER_IN:
                this.status = OrderStatus.IN_QUEUE;
                break;
            case BEVERAGE_ORDER_UP:
                this.status = OrderStatus.READY;
                break;
            case KITCHEN_ORDER_UP:
                this.status = OrderStatus.READY;
                break;
            default:
                this.status = OrderStatus.IN_QUEUE;
        }
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
