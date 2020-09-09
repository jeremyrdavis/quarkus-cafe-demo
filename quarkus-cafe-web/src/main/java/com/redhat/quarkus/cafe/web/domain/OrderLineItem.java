package com.redhat.quarkus.cafe.web.domain;

import com.redhat.quarkus.cafe.domain.Item;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
public class OrderLineItem extends PanacheEntity {

    @Enumerated(EnumType.STRING)
    Item item;

    String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id", nullable = false)
    OrderRecord orderRecord;

    public OrderLineItem() {
    }

    public OrderLineItem(OrderRecord orderRecord, Item item, String name) {
        this.orderRecord = orderRecord;
        this.item = item;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderLineItem that = (OrderLineItem) o;

        return new EqualsBuilder()
                .append(item, that.item)
                .append(name, that.name)
                .append(orderRecord, that.orderRecord)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(item)
                .append(name)
                .append(orderRecord)
                .toHashCode();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderRecord getOrderRecord() {
        return orderRecord;
    }

    public void setOrderRecord(OrderRecord orderRecord) {
        this.orderRecord = orderRecord;
    }
}
