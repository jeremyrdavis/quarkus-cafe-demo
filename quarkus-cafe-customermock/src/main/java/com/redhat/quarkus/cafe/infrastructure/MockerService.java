package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.OrderInCommand;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Creates and sends CreateOrderCommand objects to the web application
 */
@ApplicationScoped
public class MockerService {

    final Logger logger = LoggerFactory.getLogger(MockerService.class);

    @Inject
    @RestClient
    RESTService RESTService;

    public void placeOrders(OrderInCommand orderInCommand) {
        RESTService.placeOrders(orderInCommand);
    }

}
