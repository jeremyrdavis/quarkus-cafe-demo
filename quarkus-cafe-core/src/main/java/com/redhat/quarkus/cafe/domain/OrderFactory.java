package com.redhat.quarkus.cafe.domain;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Factory for creating Orders
 */
public class OrderFactory {

    public static Order createFromCreateOrderCommand(CreateOrderCommand createOrderCommand) {

        Order order = new Order();
        if (createOrderCommand.beverages.isPresent()) {

            createOrderCommand.beverages.get().forEach(b -> {
                order.getBeverageLineItems().add(b);
            });
        }else{
            order.beverageLineItems = new ArrayList<>();
        }

        if (createOrderCommand.kitchenOrders.isPresent()) {

            createOrderCommand.kitchenOrders.get().forEach(k -> {
                order.getKitchenLineItems().add(k);
            });
        }else{
            order.kitchenLineItems = new ArrayList<>();
        }
        return order;
    }
}
