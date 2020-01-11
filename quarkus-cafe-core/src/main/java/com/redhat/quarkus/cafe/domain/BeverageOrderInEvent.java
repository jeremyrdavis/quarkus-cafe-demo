package com.redhat.quarkus.cafe.domain;

public class BeverageOrderInEvent extends OrderInEvent {

    public final EventType eventType = EventType.BEVERAGE_ORDER_IN;

    public BeverageOrderInEvent(String orderId, String name, Item item) {
        super(orderId, name, item);
    }

/*
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
*/

}
