package com.redhat.quarkus.cafe.domain;

import java.util.UUID;

public class BeverageOrderInEvent implements CafeEvent {

    public String orderId;

    public final String beverageId = UUID.randomUUID().toString();

    public String name;

    public Beverage.Type item;

    public final EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public BeverageOrderInEvent(String orderId, Beverage beverage) {

        this.name = beverage.name;
        this.item = beverage.type;
        this.orderId = orderId;
    }

    public BeverageOrderInEvent(String orderId, Beverage.Type beverage, String name) {

        this.name = name;
        this.orderId = orderId;
        this.item = beverage;
    }

    public String getOrderId() {

        return orderId;
    }

    @Override
    public String getItem() {
        return item.name();
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CafeEvent.class)
                .append("[orderId=")
                .append(orderId)
                .append("beverage=")
                .append(item)
                .append("]");
        return super.toString();
    }

}
