package com.redhat.quarkus.cafe.domain;

public class Food {

    public Type type;

    public Food() {
    }

    public Food(Type type) {
        this.type = type;
    }

    public enum Type{

        CAKEPOP, CROISSANT, MUFFIN;
    }
}
