package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterForReflection
public class CreateOrderCommand {

    public String id;

    public List<LineItem> beverages = new ArrayList<>();

    public List<LineItem> kitchenOrders = new ArrayList<>();

    public CreateOrderCommand() {
    }

    public List<LineItem> getBeverages() {
        return beverages == null ? new ArrayList<LineItem>() : beverages;
    }

    public List<LineItem> getKitchenOrders() {
        return kitchenOrders == null ? new ArrayList<LineItem>() : kitchenOrders;
    }

    public CreateOrderCommand(final String id, final List<LineItem> beverages, final List<LineItem> kitchenOrders) {
        this.id = id;
        this.beverages = beverages;
        this.kitchenOrders = kitchenOrders;
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
}
