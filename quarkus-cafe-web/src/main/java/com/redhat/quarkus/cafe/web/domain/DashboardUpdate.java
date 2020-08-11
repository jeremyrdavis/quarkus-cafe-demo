package com.redhat.quarkus.cafe.web.domain;

import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.LineItemEvent;
import com.redhat.quarkus.cafe.domain.OrderStatus;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DashboardUpdate {

    public String orderId;

    public String itemId;

    public String name;

    public Item item;

    public OrderStatus status;

    public DashboardUpdate(String orderId, String name, Item item, String itemId, OrderStatus status) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
    }

    public DashboardUpdate() {
    }

    public DashboardUpdate(final LineItemEvent orderEvent) {
        this.orderId = orderEvent.orderId;
        this.itemId = orderEvent.itemId;
        this.name = orderEvent.name;
        this.item = orderEvent.item;
        switch (orderEvent.eventType) {
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
