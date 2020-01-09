package com.redhat.quarkus.cafe.domain;

import java.util.List;
import java.util.Optional;

public class CreateOrderCommand {

    public String id;

    public Optional<List<Beverage>> beverages = Optional.empty();

    public Optional<List<Food>> foods = Optional.empty();

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(String id) {

        this.id = id;
    }

    public CreateOrderCommand(String id, List<Beverage> beverages, List<Food> foods) {
        this.id = id;
        this.beverages = Optional.ofNullable(beverages);
        this.foods = Optional.ofNullable(foods);
    }

    public void addBeverages(List<Beverage> beverageList) {
        if (this.beverages.isPresent()) {

        }
        this.beverages.ifPresent(b -> b.addAll(beverageList));
        if (this.beverages.isPresent()) {
            this.beverages = Optional.of(beverageList);
        }else{
            this.beverages = Optional.of(beverageList);
        }
    }
}
