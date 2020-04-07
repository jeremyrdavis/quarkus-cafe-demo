package com.redhat.quarkus.cafe.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateOrderCommand {

    public final String id = UUID.randomUUID().toString();

    public Optional<List<LineItem>> beverages = Optional.empty();

    public Optional<List<LineItem>> kitchenOrders = Optional.empty();

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(List<LineItem> beverages, List<LineItem> kitchenOrders) {
        this.beverages = Optional.ofNullable(beverages);
        this.kitchenOrders = Optional.ofNullable(kitchenOrders);
    }

    public void addBeverages(List<LineItem> beverageList) {
        if (this.beverages.isPresent()) {
            this.beverages.get().addAll(beverageList);
        }else{
            this.beverages = Optional.of(beverageList);
        }
    }

    public void addKitchenItems(List<LineItem> kitchenOrdersList) {
        if (this.kitchenOrders.isPresent()) {
            this.kitchenOrders.get().addAll(kitchenOrdersList);
        }else{
            this.kitchenOrders = Optional.ofNullable(kitchenOrdersList);
        }
    }
}
