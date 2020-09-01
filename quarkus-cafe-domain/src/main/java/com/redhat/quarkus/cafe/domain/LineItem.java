package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
        super();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("item", item)
                .append("name", name).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LineItem lineItem = (LineItem) o;

        return new EqualsBuilder()
                .append(item, lineItem.item)
                .append(name, lineItem.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(item)
                .append(name)
                .toHashCode();
    }
}
