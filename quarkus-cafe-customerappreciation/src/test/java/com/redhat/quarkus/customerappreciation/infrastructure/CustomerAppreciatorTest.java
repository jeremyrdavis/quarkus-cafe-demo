package com.redhat.quarkus.customerappreciation.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class CustomerAppreciatorTest {

    @Inject
    CustomerAppreciator customerAppreciator;

    @Test
    public void testCustomerAppreciation() {
        String name = customerAppreciator.winner();
    }
}
