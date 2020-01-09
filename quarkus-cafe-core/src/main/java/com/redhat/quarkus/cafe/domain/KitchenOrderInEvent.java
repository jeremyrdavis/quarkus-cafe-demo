package com.redhat.quarkus.cafe.domain;

public class KitchenOrderInEvent implements CafeEvent {

    String orderId;

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
    public CafeEventType getEventType() {
        return CafeEventType.KITCHEN;
    }

}
