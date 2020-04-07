package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.internal.CriteriaImpl;

@RegisterForReflection
public class BeverageLineItemInEvent extends LineItemEvent {

    public BeverageLineItemInEvent() {
        super(EventType.BEVERAGE_ORDER_IN);
    }

    public BeverageLineItemInEvent(String orderId, String name, Item item) {
        super(EventType.BEVERAGE_ORDER_IN, orderId, name, item);
    }
}
