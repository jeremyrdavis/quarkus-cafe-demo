package com.redhat.quarkus.cafe.domain;

public interface CafeEvent {

    public String getOrderId();

    public String getItem();

    public CafeEventType getEventType();

    public enum CafeEventType{
        BEVERAGE, KITCHEN;
    }

}
