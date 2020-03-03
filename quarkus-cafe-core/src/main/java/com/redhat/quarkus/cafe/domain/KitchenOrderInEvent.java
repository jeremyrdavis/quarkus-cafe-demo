package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KitchenOrderInEvent extends OrderEvent {

    public KitchenOrderInEvent() {
        super(EventType.KITCHEN_ORDER_IN);
    }

    public KitchenOrderInEvent(String orderId, String name, Item item) {
        super(EventType.KITCHEN_ORDER_IN, orderId, name, item);
    }

}
