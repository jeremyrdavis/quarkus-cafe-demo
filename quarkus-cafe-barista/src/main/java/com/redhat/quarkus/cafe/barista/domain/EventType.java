package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum EventType {

    BEVERAGE_ORDER_IN, BEVERAGE_ORDER_UP
}
