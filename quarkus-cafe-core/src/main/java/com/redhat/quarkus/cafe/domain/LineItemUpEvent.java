package com.redhat.quarkus.cafe.domain;

public class LineItemUpEvent extends LineItemEvent {

    public LineItemUpEvent(String orderId, String name, Item item, EventType eventType) {
        super(eventType, orderId, name, item);
    }

    public LineItemUpEvent() {
    }
}
