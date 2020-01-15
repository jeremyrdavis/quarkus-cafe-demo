package com.redhat.quarkus.cafe.domain;

import java.util.Random;

public enum Beverage implements Item{


    CAPPUCCINO, COFFEE_BLACK, COFFEE_WITH_ROOM, ESPRESSO, ESPRESSO_DOUBLE;

    public static Beverage randomBeverage(){
        return values()[new Random().nextInt(values().length)];
    }
}
