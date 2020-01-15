package com.redhat.quarkus.cafe.domain;

import java.util.Random;

public enum Food implements Item{

    CAKEPOP, CROISSANT, MUFFIN;

    public static Food randomFood(){
        return values()[new Random().nextInt(values().length)];
    }

}
