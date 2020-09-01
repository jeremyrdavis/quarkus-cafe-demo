package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public abstract class WebUpdate {

    public String orderId;
    public String itemId;
    public String name;
    public Item item;
    public OrderStatus status;

    public WebUpdate() {
        super();
    }

    public WebUpdate(String orderId, String itemId, String name, Item item, OrderStatus status) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
    }


}
