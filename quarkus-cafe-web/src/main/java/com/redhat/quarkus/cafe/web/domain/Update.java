package com.redhat.quarkus.cafe.web.domain;

public class Update {

    public String orderId;

    public String customer;

    public String product;

    public String state;

    public Update() {
    }

    public Update(String id, String customer, String product, String state) {
        this.orderId = id;
        this.customer = customer;
        this.product = product;
        this.state = state;
    }
}
