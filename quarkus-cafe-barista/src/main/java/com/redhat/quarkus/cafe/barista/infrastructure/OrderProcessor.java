package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.EventType;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.StringReader;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class OrderProcessor {

    Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    @Inject @Channel("orders-out")
    Emitter<KafkaRecord<String, String>> orderUpEmitter;

    @Inject
    Barista barista;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> orderIn(Message message) {

        logger.debug("\nBarista Order In Received after registering for reflection:\n" + message);

        JsonReader reader = Json.createReader(new StringReader((String) message.getPayload()));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        if (eventType.equals(EventType.BEVERAGE_ORDER_IN.toString())) {

            logger.debug("\nBarista Order In Received after registering for reflection:\n");
            final BeverageOrder beverageOrder = jsonb.fromJson((String) message.getPayload(), BeverageOrder.class);

            barista.orderIn(beverageOrder).thenAccept(res -> {
                logger.debug("returning: {}", res);
                orderUpEmitter.send(KafkaRecord.of(res.orderId, jsonb.toJson(res)));
            });
        }
        return message.ack();
    }
}
