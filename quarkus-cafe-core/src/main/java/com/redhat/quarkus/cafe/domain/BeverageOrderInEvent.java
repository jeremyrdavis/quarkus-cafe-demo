package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BeverageOrderInEvent extends OrderEvent {

    public BeverageOrderInEvent() {
        super(EventType.BEVERAGE_ORDER_IN);
    }

    public BeverageOrderInEvent(String orderId, String name, Item item) {
        super(EventType.BEVERAGE_ORDER_IN, orderId, name, item);
    }
}
