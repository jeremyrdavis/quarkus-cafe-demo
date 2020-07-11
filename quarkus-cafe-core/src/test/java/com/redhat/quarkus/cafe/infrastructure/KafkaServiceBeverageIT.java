package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.LineItem;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest @QuarkusTestResource(CafeITResource.class)
public class KafkaServiceBeverageIT extends KafkaIT{

    @Test
    public void testOrderInBeveragesOnly() throws InterruptedException {

        List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);

        // send the order to Kafka and wait
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));
        Thread.sleep(1000);

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> newRecords = consumerMap.get("barista-in").poll(Duration.ofMillis(1000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
//            assertBeverageInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.ESPRESSO_DOUBLE) || orderInEvent.item.equals(Item.COFFEE_WITH_ROOM),
                    "The item should be either a " + Item.ESPRESSO_DOUBLE + " or a " + Item.COFFEE_WITH_ROOM + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        assertEquals(2, newRecords.count());
    }

}
