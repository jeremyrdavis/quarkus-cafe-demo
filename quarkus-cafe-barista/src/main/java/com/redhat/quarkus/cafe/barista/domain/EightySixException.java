package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.domain.*;

public class EightySixException extends Exception {

    Item item;

    public EightySixException(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
