package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CafeEvent;
import io.quarkus.test.Mock;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped @Mock
public class MockKafkaService extends KafkaService{

    public CompletableFuture<Void> produce(List<CafeEvent> cafeEventList) {

        return CompletableFuture.runAsync(() -> {
            cafeEventList.stream().forEach(cafeEvent -> {
                switch (cafeEvent.getEventType()) {
                    case BEVERAGE_ORDER_IN:
                }
                System.out.println(cafeEvent);
            });
        });

    }

}
