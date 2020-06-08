
package com.redhat.grubhub.cafe.domain;

public class GrubHubOrder {
	private String orderId;
	private String orderItem;
    private String name;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(final String orderItem) {
        this.orderItem = orderItem;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GrubHubOrder [name=" + name + ", orderId=" + orderId + ", orderItem=" + orderItem + "]";
    }
}