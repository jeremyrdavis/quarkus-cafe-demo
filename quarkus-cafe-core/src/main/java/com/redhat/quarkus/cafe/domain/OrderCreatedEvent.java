package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class OrderCreatedEvent {

    Order order;

    public List<LineItemEvent> events;

    public void addEvent(LineItemEvent orderEvent) {
        getEvents().add(orderEvent);
    }

    private List<LineItemEvent> getEvents() {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        return this.events;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addEvents(List<LineItemEvent> orderEvents) {
        getEvents().addAll(orderEvents);
    }
}
