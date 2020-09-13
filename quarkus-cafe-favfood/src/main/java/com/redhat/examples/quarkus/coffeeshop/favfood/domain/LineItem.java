package com.redhat.examples.quarkus.coffeeshop.favfood.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.StringJoiner;
import java.util.UUID;

public class LineItem {

    String itemId;

    String item;

    int quantity;

    public static LineItem createLineItem(String item, int quantity) {
        LineItem lineItem = new LineItem();
        lineItem.itemId = UUID.randomUUID().toString();
        lineItem.item = item;
        lineItem.quantity = quantity;
        return lineItem;
    }

    public LineItem() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineItem.class.getSimpleName() + "[", "]")
                .add("itemId='" + itemId + "'")
                .add("item='" + item + "'")
                .add("quantity=" + quantity)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LineItem lineItem = (LineItem) o;

        return new EqualsBuilder()
                .append(quantity, lineItem.quantity)
                .append(itemId, lineItem.itemId)
                .append(item, lineItem.item)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(itemId)
                .append(item)
                .append(quantity)
                .toHashCode();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
