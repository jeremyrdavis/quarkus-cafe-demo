package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class OrderUpEvent extends LineItemEvent{

    public String madeBy;

    public OrderUpEvent() {
        super();
    }

    public OrderUpEvent(EventType eventType, String orderId, String name, Item item, String itemId, String madeBy) {
        super(eventType, orderId, name, item, itemId);
        this.madeBy = madeBy;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }
}
