package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.barista.domain.Beverage;
import com.redhat.quarkus.cafe.barista.domain.CafeEventType;
import com.redhat.quarkus.cafe.barista.domain.Status;

public class OrderInEvent {

    public CafeEventType eventType = CafeEventType.BEVERAGE_ORDER_IN;

    public String orderId;

    public String beverageId;

    public String name;

    public Beverage beverage;

    public Status status;

    public OrderInEvent(String orderId, String beverageId, String name, Beverage beverage, Status status) {
        this.orderId = orderId;
        this.beverageId = beverageId;
        this.name = name;
        this.beverage = beverage;
    }

    public OrderInEvent() {
    }
}
