package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum Item {

    //Beverages
    CAPPUCCINO, COFFEE_BLACK, COFFEE_WITH_ROOM, ESPRESSO, ESPRESSO_DOUBLE,

    //Food
    CAKEPOP, CROISSANT, MUFFIN, CROISSANT_CHOCOLATE
}
