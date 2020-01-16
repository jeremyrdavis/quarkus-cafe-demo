package com.redhat.quarkus.cafe.domain;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomerMocker {


    public List<CreateOrderCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            CreateOrderCommand createOrderCommand = new CreateOrderCommand();
            createOrderCommand.addBeverages(createBeverages());
            createOrderCommand.addKitchenItems(createKitchenItems());
            return createOrderCommand;
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private List<Order> createBeverages() {

        List<Order> beverages = new ArrayList(2);
        beverages.add(new Order(Beverage.randomBeverage(), CustomerNames.randomName()));
        beverages.add(new Order(Beverage.randomBeverage(), CustomerNames.randomName()));
        return beverages;
    }

    private List<Order> createKitchenItems() {
        List<Order> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new Order(Food.randomFood(), CustomerNames.randomName()));
        kitchenOrders.add(new Order(Food.randomFood(), CustomerNames.randomName()));
        return kitchenOrders;
    }

}
