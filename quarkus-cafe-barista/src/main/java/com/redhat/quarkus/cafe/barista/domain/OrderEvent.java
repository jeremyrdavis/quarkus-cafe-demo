package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

@RegisterForReflection
public abstract class OrderEvent {

    public String itemId;
    public String orderId;
    public String name;
    public Item item;

    public OrderEvent() {
        super();
    }

    public OrderEvent(String itemId, String orderId, String name, Item item) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.name = name;
        this.item = item;
    }

}
