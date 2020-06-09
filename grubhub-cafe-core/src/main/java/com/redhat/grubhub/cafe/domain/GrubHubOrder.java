package com.redhat.grubhub.cafe.domain;

import java.util.List;
import com.redhat.grubhub.cafe.domain.GrubHubOrderItem;

public class GrubHubOrder {
	private String orderId;
    private String orderSource;
    private List<GrubHubOrderItem> orderItems;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(final String orderSource) {
        this.orderSource = orderSource;
    }

    public List<GrubHubOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<GrubHubOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "GrubHubOrder [orderId=" + orderId + ", orderItems=" + orderItems + ", orderSource=" + orderSource + "]";
    }
}