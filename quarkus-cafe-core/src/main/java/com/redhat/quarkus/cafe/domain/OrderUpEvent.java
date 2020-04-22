package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderUpEvent extends LineItemEvent{

    public String madeBy;

    public OrderUpEvent() {
    }

    public OrderUpEvent(EventType eventType, String orderId, String name, Item item, String madeBy) {
        super(eventType, orderId, name, item);
        this.madeBy = madeBy;
    }
}
