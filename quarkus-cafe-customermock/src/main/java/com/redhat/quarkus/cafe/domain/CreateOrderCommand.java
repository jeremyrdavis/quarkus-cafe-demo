package com.redhat.quarkus.cafe.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateOrderCommand {

    public final String id = UUID.randomUUID().toString();

    public Optional<List<Order>> beverages = Optional.empty();

    public Optional<List<Order>> kitchenOrders = Optional.empty();

    public CreateOrderCommand(List<Order> beverages, List<Order> kitchenOrders) {
        this.beverages = Optional.ofNullable(beverages);
        this.kitchenOrders = Optional.ofNullable(kitchenOrders);
    }

    public CreateOrderCommand() {
    }
}
