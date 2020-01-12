package com.redhat.quarkus.cafe.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateOrderCommand {

    public final String id = UUID.randomUUID().toString();

    public Optional<List<Order>> beverages = Optional.empty();

    public Optional<List<Order>> foods = Optional.empty();

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(List<Order> beverages, List<Order> foods) {
        this.beverages = Optional.ofNullable(beverages);
        this.foods = Optional.ofNullable(foods);
    }

    public void addBeverages(List<Order> beverageList) {
        if (this.beverages.isPresent()) {
            this.beverages.get().addAll(beverageList);
        }else{
            this.beverages = Optional.of(beverageList);
        }
    }
}
