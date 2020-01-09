package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.KafkaService;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@ApplicationScoped
public class Cafe {

    @Inject
    KafkaService kafkaService;

    //TODO Create and persist an Order
    public CompletableFuture<List<CafeEvent>> orderIn(CreateOrderCommand createOrderCommand) {

        List<CafeEvent> allEvents = new ArrayList<>();
        createOrderCommand.beverages.ifPresent(beverages -> {
            allEvents.addAll(createOrderCommand.beverages.get().stream().map(b -> new BeverageOrderInEvent(createOrderCommand.id, b)).collect(Collectors.toList()));
        });
        createOrderCommand.foods.ifPresent(foods -> {
            allEvents.addAll(createOrderCommand.foods.get().stream().map(f -> new KitchenOrderInEvent(createOrderCommand.id, f)).collect(Collectors.toList()));
        });

        return kafkaService.produce(allEvents).thenApply(v -> {
            return allEvents;
        });
    }
}
