package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;

@RegisterForReflection
public class LineItem {

    public Item item;

    public String name;

    public LineItem(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    public LineItem() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("item", item)
                .append("name", name).toString();
    }
}
