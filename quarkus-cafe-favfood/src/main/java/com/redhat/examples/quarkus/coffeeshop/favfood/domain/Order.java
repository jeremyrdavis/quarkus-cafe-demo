package com.redhat.examples.quarkus.coffeeshop.favfood.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class Order {

    final String id = UUID.randomUUID().toString();

    static final String origin = "FavFood";

    String customerName;

    List<LineItem> lineItems;

    public static Order createOrder(String customerName, List<LineItem> lineItems) {
        Order order = new Order();
        order.customerName = customerName;
        order.lineItems = lineItems;
        return order;
    }

    public Order() {
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("customerName='" + customerName + "'")
                .add("lineItems=" + lineItems)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(id, order.id)
                .append(customerName, order.customerName)
                .append(lineItems, order.lineItems)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(customerName)
                .append(lineItems)
                .toHashCode();
    }

    public String getId() {
        return id;
    }

    public static String getOrigin() {
        return origin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
