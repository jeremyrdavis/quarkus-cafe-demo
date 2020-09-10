package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.MockerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.redhat.quarkus.cafe.domain.JsonUtil.toJson;

@ApplicationScoped
public class CustomerMocker {

    final Logger logger = LoggerFactory.getLogger(CustomerMocker.class);

    @Inject
    MockerService mockerService;

    private boolean running;

    CustomerVolume customerVolume = CustomerVolume.SLOW;

    public void start() {
        this.running = true;
        logger.debug("CustomerMocker now running");
        placeOrder();
    }

    public void stop() {
        this.running = false;
        logger.debug("CustomerMocker now stopped");
    }


    public void placeOrder() {
        if (running) {
            try {
                Thread.sleep(customerVolume.getDelay() * 1000);
                int orders = new Random().nextInt(4);
                List<OrderInCommand> mockOrders = mockCustomerOrders(orders);
                logger.debug("placing orders");
                mockOrders.forEach(mockOrder -> {
                    mockerService.placeOrders(mockOrder);
                    logger.debug("placed order: {}", toJson(mockOrder));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<OrderInCommand> mockCustomerOrders(int desiredNumberOfOrders) {

        return Stream.generate(() -> {
            OrderInCommand createOrderCommand = new OrderInCommand();
            createOrderCommand.id = UUID.randomUUID().toString();
            createOrderCommand.beverages = createBeverages();
            // not all orders have kitchen items
            if (desiredNumberOfOrders % 2 == 0) {
                createOrderCommand.kitchenOrders = createKitchenItems();
            }
            return createOrderCommand;
        }).limit(desiredNumberOfOrders).collect(Collectors.toList());
    }

    private List<LineItem> createBeverages() {

        List<LineItem> beverages = new ArrayList(2);
        beverages.add(new LineItem(randomBaristaItem(), randomCustomerName()));
        beverages.add(new LineItem(randomBaristaItem(), randomCustomerName()));
        return beverages;
    }

    private List<LineItem> createKitchenItems() {
        List<LineItem> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new LineItem(randomKitchenItem(), randomCustomerName()));
        kitchenOrders.add(new LineItem(randomKitchenItem(), randomCustomerName()));
        return kitchenOrders;
    }

    Item randomBaristaItem() {
        return Item.values()[new Random().nextInt(5)];
    }

    Item randomKitchenItem() {
        return Item.values()[new Random().nextInt(3) + 5];
    }

    String randomCustomerName() {
        return CustomerNames.randomName();
    }

    public void setVolumeToBusy() {
        setCustomerVolume(CustomerVolume.BUSY);
    }

    public void setVolumeToDead() {
        setCustomerVolume(CustomerVolume.DEAD);
    }

    public void setVolumeToModerate() {
        setCustomerVolume(CustomerVolume.MODERATE);
    }

    public void setVolumeToWeeds() {
        setCustomerVolume(CustomerVolume.WEEDS);
    }

    //--------------------------------------------------
    public CustomerVolume getCustomerVolume() {
        return customerVolume;
    }

    private void setCustomerVolume(CustomerVolume customerVolume) {
        this.customerVolume = customerVolume;
    }

    public boolean isRunning() {
        return running;
    }


}
