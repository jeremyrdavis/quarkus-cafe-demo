package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderUpEvent extends BeverageOrder{

/*
    public OrderUpEvent(String orderId, String itemId, String name, Item item) {
        super(EventType.BEVERAGE_ORDER_UP, orderId, itemId, name, item);
    }
*/

    public OrderUpEvent() {
    }
}
