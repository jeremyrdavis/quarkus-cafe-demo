package com.redhat.quarkus.cafe.domain;

public class BeverageOrder {

    public Beverage beverage;

    public String name;

    public BeverageOrder(Beverage beverage, String name) {
        this.beverage = beverage;
        this.name = name;
    }
}
