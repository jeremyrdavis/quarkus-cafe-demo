package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.core.domain.Order;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@QuarkusTestResource(CafeITResource.class)
public class KafkaServiceBaristaOrderOnlyIT extends KafkaIT{
    
    @InjectMock
    OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        Mockito.doAnswer(new TestUtil.AssignIdToEntityAnswer(UUID.randomUUID().toString())).when(orderRepository).persist(any(Order.class));
    }

/*
    @Test
    public void testOrderInBeveragesOnly() throws InterruptedException {

        final List<LineItem> beverages = new ArrayList<>();
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Kirk"));
        beverages.add(new LineItem(Item.ESPRESSO_DOUBLE, "Spock"));
        final CreateOrderCommand createOrderCommand = new CreateOrderCommand(beverages, null);

        // send the order to Kafka and wait
        producerMap.get("web-in").send(new ProducerRecord("web-in", jsonb.toJson(createOrderCommand)));
        Thread.sleep(1000);

        // intercept the messages from the appropriate consumer
        final KafkaConsumer baristaConsumer = consumerMap.get("barista-in");
        baristaConsumer.seekToBeginning(new ArrayList<TopicPartition>()); //
        final ConsumerRecords<String, String> newRecords = baristaConsumer.poll(Duration.ofMillis(2000));

        // verify that the records are of the correct type
        newRecords.forEach(record -> {
            System.out.println("baristaOrder: " + record.value());
            final OrderInEvent orderInEvent = JsonUtil.jsonb.fromJson(record.value(), OrderInEvent.class);
//            assertBeverageInEvent(orderInEvent);
            assertTrue(orderInEvent.item.equals(Item.ESPRESSO_DOUBLE) || orderInEvent.item.equals(Item.COFFEE_WITH_ROOM),
                    "The item should be either a " + Item.ESPRESSO_DOUBLE + " or a " + Item.COFFEE_WITH_ROOM + " not a " + orderInEvent.item);
        });

        // verify the number of new records
        //assertEquals(2, newRecords.count());
    }
*/
}