package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.Barista;
import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import com.redhat.quarkus.cafe.barista.domain.EventType;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaResource {

    Logger logger = LoggerFactory.getLogger(KafkaResource.class);

    @Inject @Channel("orders-out")
    Emitter<KafkaRecord<String, String>> orderUpEmitter;

    private static final String TOPIC = "orders";

/*
    @ConfigProperty(name = "mp.messaging.incoming.orderin.bootstrap.servers")
    String bootstrapServers;

    @ConfigProperty(name = "mp.messaging.incoming.orderin.value.serializer")
    String serializer;

    @ConfigProperty(name = "mp.messaging.incoming.orderin.value.deserializer")
    String deserializer;

    @Inject
    Vertx vertx;

    private KafkaProducer<String, String> producer;
*/


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

//            OrderInEvent orderEvent = jsonb.fromJson(message, OrderInEvent.class);
            barista.orderIn(beverageOrder).thenAccept(res -> {

                logger.debug("returning: {}", res);
                orderUpEmitter.send(KafkaRecord.of(res.orderId, jsonb.toJson(res)));
            });
        }
        return message.ack();
    }

/*
    private void updateKafka(final BeverageOrder orderEvent) {
        System.out.println("\nNow update Kafka!");
        logger.debug("\nSending:" + orderEvent.toString());

        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(
                TOPIC,
                orderEvent.itemId,
                jsonb.toJson(orderEvent));
        System.out.println(record);
        producer.send(record, res ->{
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
        });
    }
*/


    private CompletionStage<BeverageOrder> onOrderIn(BeverageOrder beverageOrder) {

        return barista.orderIn(beverageOrder);
    }

/*
    @Incoming("ordersin")
    @Outgoing("ordersout")
    public CompletionStage<BeverageOrder> orderIn(String message) {

        logger.debug(message);

        System.out.println("order in:" + message);
        OrderInEvent orderInEvent = jsonb.fromJson(message, OrderInEvent.class);
        return barista.orderIn(orderInEvent);
    }
*/

    public class KafkaTopics{

        public static final String INCOMING = "events";
        public static final String OUTGOING = "events";
    }

/*
    @PostConstruct
    public void postConstruct() {

        // Config values can be moved to application.properties
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrapServers);
        config.put("key.serializer", serializer);
        config.put("value.serializer", serializer);
        config.put("acks", "1");
        producer = KafkaProducer.create(vertx, config);
    }
*/
}
