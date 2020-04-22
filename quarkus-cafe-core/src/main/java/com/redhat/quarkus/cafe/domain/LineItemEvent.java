package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.eventbus.EventBus;

import java.util.UUID;

@RegisterForReflection
@MongoEntity
public abstract class LineItemEvent {

    public String itemId;
    public String orderId;
    public EventType eventType;
    public String name;
    public Item item;

    public LineItemEvent() {
    }

    public LineItemEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public LineItemEvent(EventType eventType, String orderId, String name, Item item) {
        this.itemId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }
}
