package com.redhat.quarkus.customerappreciation.infrastructure;

import com.redhat.quarkus.customerappreciation.domain.Customer;
import com.redhat.quarkus.customerappreciation.domain.NoEligibleCustomersException;
import com.redhat.quarkus.customerappreciation.domain.OrderEvent;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest @Testcontainers
public class KafkaResourceIT extends BaseTestContainersIT{

    @Inject
    CustomerAppreciator customerAppreciator;


    public KafkaResourceIT() {
        super("customerappreciation", "orders-test");
    }

    public KafkaResourceIT(String producerTopic, String consumerTopic) {
        super("customerappreciation", "orders");
    }

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testPickWinnerFromIncomingEvents() {

        sendRecords();
        await().until(this::customerCount, equalTo(3));
        try {
            Customer winner = customerAppreciator.pickWinner();
            assertNotNull(winner);
        } catch (NoEligibleCustomersException e) {
            assertNull(e);
        }
    }

    private Integer customerCount() {
        return customerAppreciator.getCustomers().size();
    }


    void sendRecords() {

        logger.info("sending records");
        createOrders().forEach(orderEvent -> {

            ProducerRecord<String, String> producerRecord = new ProducerRecord(
                    PRODUCER_TOPIC,
                    orderEvent.orderId,
                    jsonb.toJson(orderEvent).toString());
        });
    }

    List<OrderEvent> createOrders(){

        ArrayList<OrderEvent> retVal = new ArrayList();

        OrderEvent minnieCappucino = new OrderEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "BEVERAGE_ORDER_IN",
                "Minnie",
                "CAPPUCCINO");

        OrderEvent mickeyCappucino = new OrderEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "BEVERAGE_ORDER_IN",
                "Mickey",
                "CAPPUCCINO");

        OrderEvent goofyBlackCoffee = new OrderEvent(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "BEVERAGE_ORDER_IN",
                "Goofey",
                "BLACK_COFFEE");

        return retVal;
    };
}
