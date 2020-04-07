package com.redhat.quarkus.cafe.domain;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class Cafe {


    public List<LineItemEvent> orderIn(CreateOrderCommand createOrderCommand) {

        List<LineItemEvent> allEvents = new ArrayList<>();
        createOrderCommand.beverages.ifPresent(beverages -> {
            allEvents.addAll(createOrderCommand.beverages.get().stream().map(b -> new BeverageLineItemInEvent(createOrderCommand.id, b.name, b.item)).collect(Collectors.toList()));
        });
        createOrderCommand.kitchenOrders.ifPresent(foods -> {
            allEvents.addAll(createOrderCommand.kitchenOrders.get().stream().map(f -> new KitchenLineItemInEvent(createOrderCommand.id, f.name, f.item)).collect(Collectors.toList()));
        });

        return allEvents;
    }

    @Transactional
    @BsonIgnore
    public OrderCreatedEvent processCreateOrderCommand(CreateOrderCommand createOrderCommand) {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        Order order = new Order();
        // add BaristaLineItems
        if (createOrderCommand.beverages.isPresent()) {

            createOrderCommand.beverages.get().forEach(b -> {
                order.addBeverageLineItem(b);
            });
        }else{
            order.beverageLineItems = new ArrayList<>();
        }
        // add KitchenLineItems
        if (createOrderCommand.kitchenOrders.isPresent()) {

            createOrderCommand.kitchenOrders.get().forEach(k -> {
            });
        }else{
            order.kitchenLineItems = new ArrayList<>();
        }

        orderCreatedEvent.setOrder(order);
        order.beverageLineItems.forEach(beverages -> {
            orderCreatedEvent.addEvent(new BeverageLineItemInEvent());
        });
        return orderCreatedEvent;
    }

}
