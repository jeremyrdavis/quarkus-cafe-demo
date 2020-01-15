package com.redhat.quarkus.cafe.domain;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomerMocker {


    public List<CreateOrderCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
/*
            CreateOrderCommand createOrderCommand = new CreateOrderCommand(
                    Arrays.asList(
                        new Order(Beverage.randomBeverage(), CustomerNames.randomNames(1).get(0)),
                        new Order(Beverage.randomBeverage(), CustomerNames.randomNames(1).get(0))));
*/
            return new CreateOrderCommand();
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }
}
