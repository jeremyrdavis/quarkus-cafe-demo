package com.redhat.quarkus.cafe.domain;

public class Order {

    public Item item;

    public String name;

    public Order(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    public Order() {
    }

}
