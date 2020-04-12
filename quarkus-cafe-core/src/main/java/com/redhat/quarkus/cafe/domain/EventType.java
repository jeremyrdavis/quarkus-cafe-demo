package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {
    BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP, KITCHEN_ORDER_IN, KITCHEN_ORDER_UP
}
