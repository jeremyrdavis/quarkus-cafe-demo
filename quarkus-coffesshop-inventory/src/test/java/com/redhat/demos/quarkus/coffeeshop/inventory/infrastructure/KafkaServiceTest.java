package com.redhat.demos.quarkus.coffeeshop.inventory.infrastructure;

import com.redhat.demos.quarkus.coffeeshop.inventory.domain.CommandType;
import com.redhat.demos.quarkus.coffeeshop.inventory.domain.RestockInventoryCommand;
import com.redhat.demos.quarkus.coffeeshop.inventory.domain.RestockItemCommand;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.infrastructure.KafkaIT;
import com.redhat.quarkus.cafe.infrastructure.KafkaTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class KafkaServiceTest extends KafkaIT {

    static final Logger logger = LoggerFactory.getLogger(KafkaServiceTest.class);

    Jsonb jsonb = JsonbBuilder.create();

    String KAKFA_TOPIC = "inventory";

    @Test
    public void testRestockRequest() {

/*
        OrderInEvent orderIn = new OrderInEvent(EventType.BEVERAGE_ORDER_IN, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Lemmy", Item.COFFEE_BLACK);
        producerMap.get("barista-in").send(new ProducerRecord<>("barista-in", jsonb.toJson(orderIn)));

 */
        RestockInventoryCommand restockInventoryCommand = new RestockInventoryCommand(Item.COFFEE_BLACK);
        producerMap.get(KAKFA_TOPIC).send(new ProducerRecord<>(KAKFA_TOPIC, jsonb.toJson(restockInventoryCommand)));

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            assertNull(e);
        }

        // Get the appropriate consumer, point to the first message, and pull all messages
        final KafkaConsumer inventoryConsumer = consumerMap.get(KAKFA_TOPIC);
        inventoryConsumer.seekToBeginning(new ArrayList<TopicPartition>());
        final ConsumerRecords<String, String> inventoryRecords = inventoryConsumer.poll(Duration.ofMillis(1000));

        assertEquals(2,inventoryRecords.count());

        int numberOfRestockInventoryCommands = 0;
        int numberOfRestockBaristaCommands = 0;

        for (ConsumerRecord<String, String> record : inventoryRecords) {
            logger.info(record.value());
            //[{"item":"COFFEE_BLACK","itemId":"901f1fb5-7ebf-4d2d-b0cd-0a80fa5a91e2","name":"Lemmy","orderId":"8a44cc4c-df49-4180-b0c5-c4ef34def5be","eventType":"BEVERAGE_ORDER_UP","madeBy":"jedavis-mac"}]
            RestockItemCommand results = jsonb.fromJson(record.value(), RestockItemCommand.class);
            assertEquals(CommandType.RESTOCK_BARISTA_COMMAND, results.commandType);
            assertEquals(Item.COFFEE_BLACK, results.getItem());
        }
        assertEquals(1, numberOfRestockBaristaCommands);
        assertEquals(1, numberOfRestockInventoryCommands);
    }
}
