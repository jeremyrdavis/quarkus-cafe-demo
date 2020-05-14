package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class CafeCoreIT {

    @Inject
    Cafe cafeCore;

    @Inject
    Cafe cafe;

    @Test
    public void testOrderInBeveragesOnly() {

        List<LineItem> beverages = new ArrayList<>();

        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);
        assertTrue(false);

/*
        try {
            cafeCore.handleCreateOrderCommand(createOrderCommand);
        } catch (ExecutionException e) {
            assertNull(e);
        } catch (InterruptedException e) {
            assertNull(e);
        }
*/

        // We'll track the number of actual events
/*
        int beverageOrderInCount = 0;

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        for (ConsumerRecord<String, String> record : newRecords) {
            LineItemEvent orderEvent = jsonb.fromJson(record.value(), LineItemEvent.class);
            if (orderEvent.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                if(orderEvent.item.equals(Item.COFFEE_WITH_ROOM)||orderEvent.item.equals(Item.ESPRESSO_DOUBLE))
                beverageOrderInCount++;
            }
        }
        assertEquals(2, beverageOrderInCount);
*/
    }
}
