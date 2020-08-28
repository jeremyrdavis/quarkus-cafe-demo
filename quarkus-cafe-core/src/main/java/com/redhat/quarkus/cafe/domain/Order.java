package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class Order {

    @Transient
    static final Logger logger = LoggerFactory.getLogger(Order.class);

    @BsonId
    public String id;

    public List<LineItem> beverageLineItems = new ArrayList<>();

    public List<LineItem> kitchenLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(List<LineItem> beverageLineItems) {
        this.beverageLineItems = beverageLineItems;
    }

    public static OrderCreatedEvent processCreateOrderCommand(OrderInCommand orderInCommand) {
        Order order = createOrderFromCommand(orderInCommand);
        return createOrderCreatedEvent(order);
    }

    /*
        Creates the Value Objects associated with a new Order
     */
    private static OrderCreatedEvent createOrderCreatedEvent(final Order order) {
        // construct the OrderCreatedEvent
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.order = order;
        if (order.getBeverageLineItems().size() >= 1) {
            order.beverageLineItems.forEach(b -> {
                orderCreatedEvent.addEvent(new OrderInEvent(EventType.BEVERAGE_ORDER_IN, order.id, b.name, b.item));
            });
        }
        if (order.getKitchenLineItems().size() >= 1) {
            order.kitchenLineItems.forEach(k -> {
                orderCreatedEvent.addEvent(new OrderInEvent(EventType.KITCHEN_ORDER_IN, order.id, k.name, k.item));
            });
        }
        logger.debug("createEventFromCommand: returning OrderCreatedEvent {}", orderCreatedEvent.toString());
        return orderCreatedEvent;
    }

    private static Order createOrderFromCommand(final OrderInCommand orderInCommand) {
        logger.debug("createOrderFromCommand: CreateOrderCommand {}", orderInCommand.toString());

        // build the order from the CreateOrderCommand
        Order order = new Order();
        order.id = orderInCommand.id;
        if (orderInCommand.getBeverages().size() >= 1) {
            logger.debug("createOrderFromCommand adding beverages {}", orderInCommand.beverages.size());
            orderInCommand.beverages.forEach(b -> {
                logger.debug("createOrderFromCommand adding beverage {}", b.toString());
                order.getBeverageLineItems().add(new LineItem(b.item, b.name));
            });
        }
        if (orderInCommand.getKitchenOrders().size() >= 1) {
            logger.debug("createOrderFromCommand adding kitchenOrders {}", orderInCommand.kitchenOrders.size());
            orderInCommand.kitchenOrders.forEach(k -> {
                logger.debug("createOrderFromCommand adding kitchenOrder {}", k.toString());
                order.getKitchenLineItems().add(new LineItem(k.item, k.name));
            });
        }
        return order;
    }

    public List<LineItem> getBeverageLineItems() {
        return beverageLineItems;
    }

    public List<LineItem> getKitchenLineItems() {
        return kitchenLineItems;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id:", this.id)
                .append("beverageLineItems", beverageLineItems.toString())
                .append("kitchenLineItems", kitchenLineItems.toString()).toString();
    }
}
