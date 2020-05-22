package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderInEvent extends LineItemEvent{

    public OrderInEvent() {
    }

    public OrderInEvent(EventType eventType, String orderId, String name, Item item) {
        super(eventType, orderId, name, item);
    }

    public OrderInEvent(EventType eventType, String orderId, String itemId, String name, Item item) {
        super(eventType, orderId, name, item);
    }
}
