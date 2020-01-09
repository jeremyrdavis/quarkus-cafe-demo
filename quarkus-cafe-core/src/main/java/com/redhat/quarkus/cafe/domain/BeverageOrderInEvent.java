package com.redhat.quarkus.cafe.domain;

public class BeverageOrderInEvent implements CafeEvent {

    String orderId;

    Beverage beverage;

    public BeverageOrderInEvent(String orderId, Beverage beverage) {

        this.orderId = orderId;
        this.beverage = beverage;
    }

    public String getOrderId() {

        return orderId;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CafeEvent.class)
                .append("[orderId=")
                .append(orderId)
                .append("beverage=")
                .append(beverage)
                .append("]");
        return super.toString();
    }

    public String getItem() {
        return this.beverage.type.toString();
    }

    public CafeEventType getEventType() {
        return CafeEventType.BEVERAGE;
    }
}
