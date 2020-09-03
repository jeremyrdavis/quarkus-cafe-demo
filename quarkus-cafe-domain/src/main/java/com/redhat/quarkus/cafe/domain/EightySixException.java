package com.redhat.quarkus.cafe.domain;

import java.util.Arrays;
import java.util.Collection;

/**
 * Thrown when an item is out of stock
 */
public class EightySixException extends Exception {

    Item item;

    public EightySixException(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Collection<EightySixEvent> getEvensa() {
        return Arrays.asList(new EightySixEvent(item));
    }
}
