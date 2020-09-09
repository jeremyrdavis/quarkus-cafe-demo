package com.redhat.quarkus.cafe.web.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import java.util.StringJoiner;

@Entity
public class OrderRecord extends PanacheEntity {

    String orderId;

    String orderSource = "WEB";

    String payload;

    public OrderRecord() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderRecord.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("orderSource='" + orderSource + "'")
                .add("payload='" + payload + "'")
                .add("id=" + id)
                .toString();
    }

    public OrderRecord(String orderId, String payload) {
        this.orderId = orderId;
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderRecord that = (OrderRecord) o;

        return new EqualsBuilder()
                .append(orderId, that.orderId)
                .append(orderSource, that.orderSource)
                .append(payload, that.payload)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orderId)
                .append(orderSource)
                .append(payload)
                .toHashCode();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
