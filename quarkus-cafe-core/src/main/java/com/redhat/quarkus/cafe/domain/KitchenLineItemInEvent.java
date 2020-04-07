package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KitchenLineItemInEvent extends LineItemEvent {

    public KitchenLineItemInEvent() {
        super(EventType.KITCHEN_ORDER_IN);
    }

    public KitchenLineItemInEvent(String orderId, String name, Item item) {
        super(EventType.KITCHEN_ORDER_IN, orderId, name, item);
    }

}
