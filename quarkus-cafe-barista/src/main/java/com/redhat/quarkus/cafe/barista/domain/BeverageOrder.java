package com.redhat.quarkus.cafe.barista.domain;

public class BeverageOrder {

    public String orderId;

    public String name;

    public Beverage product;

    public Status status;

    public BeverageOrder() {
    }

    public BeverageOrder(String orderId, String name, Beverage product) {
        this.orderId = orderId;
        this.name = name;
        this.product = product;
    }

    public BeverageOrder(String orderId, String name, Beverage product, Status status) {
        this.orderId = orderId;
        this.name = name;
        this.product = product;
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("BeverageOrder[")
                .append("name=")
                .append(name)
                .append(",beverage=")
                .append(product)
                .append(",status=")
                .append(status)
                .append("]")
                .toString();
    }
}
