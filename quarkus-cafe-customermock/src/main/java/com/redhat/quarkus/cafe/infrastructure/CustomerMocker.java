package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import com.redhat.quarkus.cafe.infrastructure.OrderService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.toJson;

/**
 * Creates and sends CreateOrderCommand objects to the web application
 */
@ApplicationScoped
public class CustomerMocker {

    final Logger logger = LoggerFactory.getLogger(CustomerMocker.class);

    @Inject
    @RestClient
    OrderService orderService;

    @Scheduled(every = "5s")
    public void placeOrder() {
//        int seconds = new Random().nextInt(5);
/*
        try {
*/
//            Thread.sleep(seconds * 5000);
            int orders = new Random().nextInt(5);
            List<CreateOrderCommand> mockOrders = mockCustomerOrders(orders);
            mockOrders.forEach(mockOrder -> {
                orderService.placeOrders(mockOrder);
                logger.debug("placed order: {}", toJson(mockOrder));
            });
/*
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
    }


    public List<CreateOrderCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            CreateOrderCommand createOrderCommand = new CreateOrderCommand();
            createOrderCommand.addBeverages(createBeverages());
            // not all orders have kitchen items
            if (desiredNumberOfOrders % 2 == 0) {
                createOrderCommand.addKitchenItems(createKitchenItems());
            }
            return createOrderCommand;
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private List<Order> createBeverages() {

        List<Order> beverages = new ArrayList(2);
        beverages.add(new Order(Beverage.randomBeverage(), CustomerNames.randomName()));
        beverages.add(new Order(Beverage.randomBeverage(), CustomerNames.randomName()));
        return beverages;
    }

    private List<Order> createKitchenItems() {
        List<Order> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new Order(Food.randomFood(), CustomerNames.randomName()));
        kitchenOrders.add(new Order(Food.randomFood(), CustomerNames.randomName()));
        return kitchenOrders;
    }

}
