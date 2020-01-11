package com.redhat.quarkus.cafe.domain;

public class Beverage {

    public Type type;

    public String name;

    public Beverage(Type typeToSet, String nameToSet) {
        this.type = typeToSet; this.name = nameToSet;
    }

    public Beverage() {
    }

    public enum Type{
        COFFEE_BLACK, COFFEE_WITH_ROOM, CAPUCCINO, ESPRESSO, ESPRESSO_DOUBLE;
    }
}
