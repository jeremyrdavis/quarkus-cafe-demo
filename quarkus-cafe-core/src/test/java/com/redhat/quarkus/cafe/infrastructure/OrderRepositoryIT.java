package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.zookeeper.Op;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@QuarkusTest
@QuarkusTestResource(CafeITResource.class)
public class OrderRepositoryIT {

    @Inject
    OrderRepository orderRepository;

    @Test
    public void testPersistingOrder(){

        CreateOrderCommand createOrderCommand = mockCreateOrderCommand();
        OrderCreatedEvent orderCreatedEvent = Order.processCreateOrderCommand(createOrderCommand);
        Order order = orderCreatedEvent.order;
        try {
            orderRepository.persist(order);
        } catch (Exception e) {
            assertNull(e);
            System.out.println(e.getMessage());
        }
        assertNotNull(order.id);
    }

    private CreateOrderCommand mockCreateOrderCommand() {
        final List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));

        final List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Harry"));
        menuItems.add(new LineItem(Item.MUFFIN, "Hermione"));

        return new CreateOrderCommand(UUID.randomUUID().toString(),beverages, menuItems);
    }

}
