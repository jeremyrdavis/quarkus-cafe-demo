package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderReadyUpdate extends WebUpdate {

    public String madeBy;

    public OrderReadyUpdate() {
        super();
    }

    public OrderReadyUpdate(String orderId, String itemId, String name, Item item, OrderStatus status, String madeBy) {
        super(orderId, itemId, name, item, status);
        this.madeBy = madeBy;
    }

    public OrderReadyUpdate(OrderUpEvent orderUpEvent) {
        this.orderId = orderUpEvent.orderId;
        this.itemId = orderUpEvent.itemId;
        this.name = orderUpEvent.name;
        this.item = orderUpEvent.item;
        this.madeBy = orderUpEvent.madeBy;
        this.status = OrderStatus.READY;
    }

    public OrderReadyUpdate(OrderInEvent orderInEvent) {
        this.orderId = orderInEvent.orderId;
        this.itemId = orderInEvent.itemId;
        this.name = orderInEvent.name;
        this.item = orderInEvent.item;
        this.status = OrderStatus.READY;
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
