package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * This class represents all orders in the system
 */
@RegisterForReflection
public abstract class OrderEvent {

    public String itemId;
    public String orderId;
    public String name;
    public Item item;

    public OrderEvent(String orderId, String name, Item item, String itemId) {
        this.orderId = orderId;
        this.name = name;
        this.item = item;
        this.itemId = itemId;
    }

    public OrderEvent() {
    }
}
