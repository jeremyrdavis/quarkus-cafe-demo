package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import com.redhat.quarkus.cafe.web.domain.Item;
import com.redhat.quarkus.cafe.web.domain.OrderStatus;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;

public class KafkaResourceIT extends BaseTestContainersIT{

    static final String PRODUCER_TOPIC = "orders-test";

    static final String CONSUMER_TOPIC = "updates-test";

    @Inject
    KafkaResource kafkaResource;


    public KafkaResourceIT() {
        super(PRODUCER_TOPIC, CONSUMER_TOPIC);
    }

    @Test
    public void testSingleUpdate() {

        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Minnie",
                Item.CAPPUCCINO,
                OrderStatus.IN_QUEUE);

        ProducerRecord<String, String> producerRecord = new ProducerRecord(
                PRODUCER_TOPIC,
                dashboardUpdate.orderId,
                jsonb.toJson(dashboardUpdate).toString());

        kafkaProducer.send(producerRecord);
    }
}
