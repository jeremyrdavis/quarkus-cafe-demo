package com.redhat.quarkus.customerappreciation.domain;

public class CustomerAppreciationEvent {


    public static final String eventType = "CUSTOMER_APPRECIATION_EVENT";

    public String customerName;

    public CustomerAppreciationEvent() {
    }

    public CustomerAppreciationEvent(String customerName) {
        this.customerName = customerName;
    }
}
