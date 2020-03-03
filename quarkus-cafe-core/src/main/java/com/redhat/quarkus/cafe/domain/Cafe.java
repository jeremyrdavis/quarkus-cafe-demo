package com.redhat.quarkus.cafe.domain;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class Cafe {

    public List<OrderEvent> orderIn(CreateOrderCommand createOrderCommand) throws ExecutionException, InterruptedException {

        List<OrderEvent> allEvents = new ArrayList<>();
        createOrderCommand.beverages.ifPresent(beverages -> {
            allEvents.addAll(createOrderCommand.beverages.get().stream().map(b -> new BeverageOrderInEvent(createOrderCommand.id, b.name, b.item)).collect(Collectors.toList()));
        });
        createOrderCommand.kitchenOrders.ifPresent(foods -> {
            allEvents.addAll(createOrderCommand.kitchenOrders.get().stream().map(f -> new KitchenOrderInEvent(createOrderCommand.id, f.name, f.item)).collect(Collectors.toList()));
        });

        return allEvents;
    }
}
