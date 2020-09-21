package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Arrays;
import java.util.Collection;

/**
 * Thrown when an item is out of stock
 */
@RegisterForReflection
public class EightySixException extends Exception {

    Item item;

    public EightySixException(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Collection<EightySixEvent> getEvents() {
        return Arrays.asList(new EightySixEvent(item));
    }
}
