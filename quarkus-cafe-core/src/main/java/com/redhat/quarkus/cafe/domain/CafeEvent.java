package com.redhat.quarkus.cafe.domain;

public interface CafeEvent {

    public String getOrderId();

    public String getItem();

    public EventType getEventType();

    public enum EventType {
        BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP, KITCHEN_ORDER_IN, KITCHEN_ORDER_UP;
    }

}
