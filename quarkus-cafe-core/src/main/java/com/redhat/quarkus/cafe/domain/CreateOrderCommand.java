package com.redhat.quarkus.cafe.domain;

import java.util.List;
import java.util.Optional;

public class CreateOrderCommand {

    public String id;

    public Optional<List<Order>> beverages = Optional.empty();

    public Optional<List<Order>> foods = Optional.empty();

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(String id) {

        this.id = id;
    }

    public CreateOrderCommand(String id, List<Order> beverages, List<Order> foods) {
        this.id = id;
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
