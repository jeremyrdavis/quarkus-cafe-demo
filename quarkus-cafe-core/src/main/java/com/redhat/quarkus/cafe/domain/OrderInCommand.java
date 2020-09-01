package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class OrderInCommand {

    public String id;

    public List<LineItem> beverages = new ArrayList<>();

    public List<LineItem> kitchenOrders = new ArrayList<>();

    public OrderInCommand() {
    }

    public OrderInCommand(final String id, final List<LineItem> beverages, final List<LineItem> kitchenOrders) {
        this.id = id;
        this.beverages = beverages;
        this.kitchenOrders = kitchenOrders;
    }

    public List<LineItem> getBeverages() {
        return beverages == null ? new ArrayList<LineItem>() : beverages;
    }

    public List<LineItem> getKitchenOrders() {
        return kitchenOrders == null ? new ArrayList<LineItem>() : kitchenOrders;
    }

    public void addBeverages(final String id, final List<LineItem> beverageList) {
        this.id = id;
        this.beverages.addAll(beverageList);
    }

    public void addKitchenItems(final String id, final List<LineItem> kitchenOrdersList) {
        this.id = id;
        this.kitchenOrders.addAll(kitchenOrdersList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("beverages", beverages)
                .append("kitchenOrders", kitchenOrders)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderInCommand that = (OrderInCommand) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(beverages, that.beverages)
                .append(kitchenOrders, that.kitchenOrders)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(beverages)
                .append(kitchenOrders)
                .toHashCode();
    }
}
