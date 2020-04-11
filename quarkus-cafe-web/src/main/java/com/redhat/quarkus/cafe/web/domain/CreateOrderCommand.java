package com.redhat.quarkus.cafe.web.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterForReflection
public class CreateOrderCommand {

    public final String id = UUID.randomUUID().toString();

    public Optional<List<Order>> beverages = Optional.empty();

    public Optional<List<Order>> kitchenOrders = Optional.empty();

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(List<Order> beverages, List<Order> kitchenOrders) {
        this.beverages = Optional.ofNullable(beverages);
        this.kitchenOrders = Optional.ofNullable(kitchenOrders);
    }

    public void addBeverages(List<Order> beverageList) {
        if (this.beverages.isPresent()) {
            this.beverages.get().addAll(beverageList);
        }else{
            this.beverages = Optional.of(beverageList);
        }
    }

    public void addKitchenItems(List<Order> kitchenOrdersList) {
        if (this.kitchenOrders.isPresent()) {
            this.kitchenOrders.get().addAll(kitchenOrdersList);
        }else{
            this.kitchenOrders = Optional.ofNullable(kitchenOrdersList);
        }
    }
}
