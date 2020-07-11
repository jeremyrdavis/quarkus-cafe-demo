package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.OrderRepository;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class CafeTest {

    Logger logger = LoggerFactory.getLogger(CafeTest.class);

    @Inject
    Cafe cafe;

    @InjectMock
    OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        Mockito.doAnswer(new AssignIdToEntityAnswer(1L)).when(orderRepository).persist(any(Order.class));
    }

    @Test
    public void testProcessCreateOrderCommandBeveragesOnly() {

        QuarkusMock.installMockForType(orderRepository, OrderRepository.class);

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);
        System.out.println(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = cafe.processCreateOrderCommand(createOrderCommand);

        assertNotNull(orderCreatedEvent);
        assertNotNull(orderCreatedEvent.events);
        assertEquals(2, orderCreatedEvent.events.size());
        orderCreatedEvent.events.forEach(e -> {
            assertEquals(OrderInEvent.class, e.getClass());
            assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
            assertEquals(EventType.BEVERAGE_ORDER_IN, e.eventType);
        });
    }

    class AssignIdToEntityAnswer implements Answer<Void> {

        private final Long id;

        public AssignIdToEntityAnswer(Long id) {
            this.id = id;
        }

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Order order = (Order) invocation.getArguments()[0];
            order.id = id;
            return null;
        }
    }

}
