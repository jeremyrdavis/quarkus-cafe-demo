package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.OrderService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomerMocker {

    @Inject
    @RestClient
    OrderService orderService;

    @Scheduled(every = "30s")
    public void placeOrder() {
        int seconds = new Random().nextInt(20);
        try {
            Thread.sleep(seconds * 1000);
            int orders = new Random().nextInt(5);
            List<CreateOrderCommand> mockOrders = mockCustomerOrders(orders);
            mockOrders.forEach(mockOrder -> {
                orderService.placeOrders(mockOrder);
                System.out.println("\nplaced order\n");
                System.out.println(mockOrder.toString());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<CreateOrderCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            CreateOrderCommand createOrderCommand = new CreateOrderCommand();
            createOrderCommand.addBeverages(createBeverages());
            createOrderCommand.addKitchenItems(createKitchenItems());
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
