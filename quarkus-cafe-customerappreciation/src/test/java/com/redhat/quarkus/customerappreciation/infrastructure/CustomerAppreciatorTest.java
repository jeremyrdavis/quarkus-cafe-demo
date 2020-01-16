package com.redhat.quarkus.customerappreciation.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest @Testcontainers
public class CustomerAppreciatorTest extends BaseTestContainersIT{

    @Inject
    CustomerAppreciator customerAppreciator;

    @Test
    public void testCustomerAppreciation() {

        createOrders();
    }

    private void createOrders() {
    }
}
