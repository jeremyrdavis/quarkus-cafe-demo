package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderInEvent extends BeverageOrder{

/*
    public OrderInEvent(String orderId, String itemId, String name, Item item) {
        super(EventType.BEVERAGE_ORDER_IN, orderId, itemId, name, item);
    }
*/

    public OrderInEvent() {
    }
}
