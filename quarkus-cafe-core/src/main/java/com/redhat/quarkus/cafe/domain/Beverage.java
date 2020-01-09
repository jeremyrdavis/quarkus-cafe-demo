package com.redhat.quarkus.cafe.domain;

public class Beverage {

    public Type type;

    public Beverage(Type typeToSet) {
        this.type = typeToSet;
    }

    public Beverage() {
    }

    public enum Type{
        COFFEE_BLACK, COFFEE_WITH_ROOM, CAPUCCINO, ESPRESSO, ESPRESSO_DOUBLE;
    }
}
