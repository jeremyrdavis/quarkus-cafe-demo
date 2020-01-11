package com.redhat.quarkus.cafe.domain;

import java.util.UUID;

public class KitchenOrderInEvent implements CafeEvent {

    public String orderId;

    public final String beverageId = UUID.randomUUID().toString();

    public String name;

    Food food;

    public KitchenOrderInEvent(String orderId, Food food) {
        this.orderId = orderId;
        this.food = food;
    }

    @Override
    public String getOrderId() {
        return this.orderId;
    }

    @Override
    public String getItem() {
        return this.food.type.toString();
    }

    @Override
    public EventType getEventType() {
        return EventType.KITCHEN_ORDER_IN;
    }

}
