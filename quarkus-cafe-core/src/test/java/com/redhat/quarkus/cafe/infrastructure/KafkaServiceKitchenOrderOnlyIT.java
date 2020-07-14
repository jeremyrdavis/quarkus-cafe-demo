package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@QuarkusTestResource(CafeITResource.class)
public class KafkaServiceKitchenOrderOnlyIT extends KafkaIT{

    @InjectMock
    OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        Mockito.doAnswer(new TestUtil.AssignIdToEntityAnswer(1L)).when(orderRepository).persist(any(Order.class));
    }

    @Test
    public void testOrderInKitchenOnly() throws InterruptedException{

        final List<LineItem> menuItems = new ArrayList<>();
        menuItems.add(new LineItem(Item.CAKEPOP, "Mickey"));
        menuItems.add(new LineItem(Item.MUFFIN, "Goofy"));
        final CreateOrderCommand createOrderCommand = new CreateOrderCommand(null, menuItems);

        // send the order to Kafka
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));

        Thread.sleep(2000);

        // Get the appropriate consumer, point to the first message, and pull all messages
        final KafkaConsumer kitchenConsumer = consumerMap.get("kitchen-in");
        kitchenConsumer.seekToBeginning(new ArrayList<TopicPartition>()); //
        final ConsumerRecords<String, String> newRecords = kitchenConsumer.poll(Duration.ofMillis(5000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println(record.value());
            final OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
            assertEquals(EventType.KITCHEN_ORDER_IN, orderInEvent.eventType);
            assertTrue(orderInEvent.item.equals(Item.CAKEPOP) || orderInEvent.item.equals(Item.MUFFIN),
                    "The item should be either a " + Item.MUFFIN + " or a " + Item.CAKEPOP + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        //assertEquals(2, newRecords.count());

    }
}