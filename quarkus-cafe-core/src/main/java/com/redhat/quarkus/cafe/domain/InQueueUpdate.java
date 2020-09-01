package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class InQueueUpdate extends WebUpdate {

    public InQueueUpdate() {
    }

    public InQueueUpdate(String orderId, String itemId, String name, Item item, OrderStatus status) {
        super(orderId, itemId, name, item, status);
    }

    public InQueueUpdate(final LineItemEvent lineItemEvent) {
        super(lineItemEvent.orderId, lineItemEvent.itemId, lineItemEvent.name, lineItemEvent.item, OrderStatus.IN_QUEUE);
    }

}
