package com.redhat.quarkus.cafe.kitchen.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KitchenOrder {

    String orderNumber;

    String name;

    MenuItem menuItem;

    OrderStatus status;

    public KitchenOrder() {
    }

    public KitchenOrder(String orderNumber, String name, MenuItem menuItem, OrderStatus status) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.menuItem = menuItem;
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Order[")
                .append("orderNumber=")
                .append(orderNumber)
                .append(",name=")
                .append(name)
                .append(",menuItem=")
                .append(menuItem)
                .append(",status=")
                .append(status).toString();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
