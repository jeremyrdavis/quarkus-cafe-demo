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

@QuarkusTest
@QuarkusTestResource(CafeITResource.class)
public class KafkaServiceKitchenIT extends KafkaIT{

    @Test
    public void testOrderInKitchenOnly() throws InterruptedException{

        List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Mickey"));
        menuItems.add(new LineItem(Item.MUFFIN, "Goofy"));
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, menuItems);

        // send the order to Kafka
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));

        Thread.sleep(2000);

        // intercept the messages from the appropriate consumer
        ConsumerRecords<String, String> newRecords = consumerMap.get("kitchen-in").poll(Duration.ofMillis(5000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println(record.value());
            OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
//            assertKitchenInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.CAKEPOP) || orderInEvent.item.equals(Item.MUFFIN),
                    "The item should be either a " + Item.MUFFIN + " or a " + Item.CAKEPOP + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        assertEquals(2, newRecords.count());

    }
}
