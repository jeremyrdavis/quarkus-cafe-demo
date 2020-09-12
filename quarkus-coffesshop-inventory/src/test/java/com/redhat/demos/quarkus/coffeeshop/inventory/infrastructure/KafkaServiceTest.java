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
            RestockItemCommand restockItemCommand = jsonb.fromJson(record.value(), RestockItemCommand.class);
//            assertEquals(CommandType.RESTOCK_BARISTA_COMMAND, restockItemCommand.commandType);
//            assertEquals(Item.COFFEE_BLACK, restockItemCommand.getItem());
            if (CommandType.RESTOCK_BARISTA_COMMAND.equals(restockItemCommand.commandType)) {
                numberOfRestockBaristaCommands++;
            }
            if (CommandType.RESTOCK_INVENTORY_COMMAND.equals(restockItemCommand.commandType)) {
                numberOfRestockInventoryCommands++;
            }
        }
        assertEquals(1, numberOfRestockBaristaCommands);
        assertEquals(1, numberOfRestockInventoryCommands);
    }
}
