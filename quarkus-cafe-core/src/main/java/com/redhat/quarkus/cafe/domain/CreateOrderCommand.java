package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterForReflection
public class CreateOrderCommand {

    public final String id = UUID.randomUUID().toString();

    public List<LineItem> beverages = new ArrayList<>();

    public List<LineItem> kitchenOrders = new ArrayList<>();

    public CreateOrderCommand() {
    }

    public List<LineItem> getBeverages() {
        return beverages;
    }

    public List<LineItem> getKitchenOrders() {
        return kitchenOrders;
    }

    public CreateOrderCommand(List<LineItem> beverages, List<LineItem> kitchenOrders) {
        this.beverages = beverages;
        this.kitchenOrders = kitchenOrders;
    }

    public void addBeverages(List<LineItem> beverageList) {
        this.beverages.addAll(beverageList);
    }

    public void addKitchenItems(List<LineItem> kitchenOrdersList) {
        this.kitchenOrders.addAll(kitchenOrdersList);
    }
}
