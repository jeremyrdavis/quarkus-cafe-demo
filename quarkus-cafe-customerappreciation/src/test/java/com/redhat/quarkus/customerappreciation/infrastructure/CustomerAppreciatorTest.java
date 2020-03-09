package com.redhat.quarkus.customerappreciation.infrastructure;

import com.redhat.quarkus.customerappreciation.domain.Customer;
import com.redhat.quarkus.customerappreciation.domain.CustomerStatus;
import com.redhat.quarkus.customerappreciation.domain.NoEligibleCustomersException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CustomerAppreciatorTest{

    @Inject
    CustomerAppreciator customerAppreciator;

    @BeforeEach
    public void setUp() {

        // Remove any existing customers
        customerAppreciator.getCustomers().clear();
        assertEquals(0, customerAppreciator.getCustomers().size());
    }

    @Test
    public void addCustomer() {

        int size = customerAppreciator.getCustomers().size() + 1;

        customerAppreciator.addCustomer("Paul P.");
        assertEquals(size, customerAppreciator.getCustomers().keySet().size());
    }

    @Test
    public void pickWinner() {

        customerAppreciator.addCustomer("Bruno F.");
        customerAppreciator.addCustomer("Scott McT");
        customerAppreciator.addCustomer("Marcus R.");

        try {

            Customer customer = customerAppreciator.pickWinner();
            assertNotNull(customer);
            assertEquals(CustomerStatus.WINNER, customer.customerStatus);

            int winningCustomers = (int) customerAppreciator.getCustomers().values().stream().filter(c -> c.customerStatus.equals(CustomerStatus.WINNER)).count();
            assertEquals(1, winningCustomers);
            assertEquals(2, customerAppreciator.getCustomers().keySet().size() - winningCustomers);

        } catch (NoEligibleCustomersException e) {

            assertNull(e);
        }
    }

    @Test
    public void pickWinnerNoCustomers() {

        boolean exceptionThrown = false;

        try {

            Customer customer = customerAppreciator.pickWinner();
            assertNotNull(customer);
        } catch (NoEligibleCustomersException nece) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
