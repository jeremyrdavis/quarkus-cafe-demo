package com.redhat.quarkus.cafe.core.domain;

import com.redhat.quarkus.cafe.domain.LineItemEvent;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class OrderCreatedEvent {

    public Order order;

    public List<LineItemEvent> events = new ArrayList<>();

    public void addEvent(LineItemEvent orderEvent) {
        getEvents().add(orderEvent);
    }

    public List<LineItemEvent> getEvents() {
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
