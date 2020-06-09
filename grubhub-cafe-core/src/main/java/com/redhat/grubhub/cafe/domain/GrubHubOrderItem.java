package com.redhat.grubhub.cafe.domain;

public class GrubHubOrderItem {
	private String orderItem;
    private String name;

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
		this.name = name;
    }

    @Override
    public String toString() {
        return "GrubHubOrderItem [name=" + name + ", orderItem=" + orderItem + "]";
    }
    
}
    