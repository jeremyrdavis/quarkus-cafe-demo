package com.redhat.quarkus.cafe.domain;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

@QuarkusTest
public class CustomerMockerTest {


    @Inject
    CustomerMocker customerMocker;

    @Test
    public void testCustomerMocker() {

        List<CreateOrderCommand> createOrderCommands = customerMocker.mockCustomerOrders(15);
    }
}
