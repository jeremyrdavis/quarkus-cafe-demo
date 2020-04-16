package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RegisterForReflection
@MongoEntity
public class Order extends PanacheMongoEntity {

    static final Logger logger = LoggerFactory.getLogger(Order.class);

    public List<LineItem> beverageLineItems = new ArrayList<>();

    public List<LineItem> kitchenLineItems = new ArrayList<>();

    public List<LineItem> getBeverageLineItems() {
        return beverageLineItems;
    }

    public List<LineItem> getKitchenLineItems() {
        return kitchenLineItems;
    }

    public static CompletableFuture<OrderCreatedEvent> processCreateOrderCommand(final CreateOrderCommand createOrderCommand) {
        logger.debug("processCreateOrderCommand: processing {}", createOrderCommand.toString());
        return CompletableFuture.supplyAsync(() -> createEventFromCommand(createOrderCommand));
    }

    private static OrderCreatedEvent createEventFromCommand(final CreateOrderCommand createOrderCommand) {

        final Order order = createOrderFromCommand(createOrderCommand);
        logger.debug("createEventFromCommand: Order created {}", order.toString());

        // construct the OrderCreatedEvent
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.order = order;
        if (order.beverageLineItems != null) {
            order.beverageLineItems.forEach(b -> {
                orderCreatedEvent.addEvent(new LineItemEvent(EventType.BEVERAGE_ORDER_IN, order.id.toString(), b.name, b.item));
            });
        }
        if (order.kitchenLineItems != null) {
            order.kitchenLineItems.forEach(k -> {
                orderCreatedEvent.addEvent(new LineItemEvent(EventType.KITCHEN_ORDER_IN, order.id.toString(), k.name, k.item));
            });
        }
        logger.debug("createEventFromCommand: returning OrderCreatedEvent {}", orderCreatedEvent.toString());
        return orderCreatedEvent;
    }

    private static Order createOrderFromCommand(final CreateOrderCommand createOrderCommand) {
        logger.debug("createOrderFromCommand: CreateOrderCommand {}", createOrderCommand.toString());

        // build the order from the CreateOrderCommand
        Order order = new Order();
        if (createOrderCommand.beverages.size() >= 1) {

            createOrderCommand.beverages.forEach(b -> {
                order.getBeverageLineItems().add(b);
            });
        }else{
            order.beverageLineItems = new ArrayList<>();
        }

        if (createOrderCommand.kitchenOrders.size() >= 1) {

            createOrderCommand.kitchenOrders.forEach(k -> {
                order.getKitchenLineItems().add(k);
            });
        }else{
            order.kitchenLineItems = new ArrayList<>();
        }

        // persist the order
        logger.debug("createOrderFromCommand: persisting {}", order.toString());
        order.persist();
        logger.debug("createOrderFromCommand: persisted {}", order.toString());
        return order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id:", this.id.toString()).toString();
/*                .append("beverageLineItems", beverageLineItems.toString())
                .append("kitchenLineItems", kitchenLineItems.toString()).toString();*/
    }
}
