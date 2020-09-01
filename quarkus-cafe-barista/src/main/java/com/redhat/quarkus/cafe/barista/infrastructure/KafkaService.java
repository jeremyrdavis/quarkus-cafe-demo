package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import com.redhat.quarkus.cafe.barista.domain.Barista;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped @RegisterForReflection
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    Barista barista;

    @Inject @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> handleOrderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());
        final OrderInEvent orderIn = jsonb.fromJson((String) message.getPayload(), OrderInEvent.class);
        if (orderIn.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
            return barista.make(orderIn).thenApply(o -> {
                return orderUpEmitter.send(jsonb.toJson(o));
            }).thenRun( () -> { message.ack(); });
        }else{
            return message.ack();
        }
    }

    CompletableFuture<Void> sendOrderUpEvent(final OrderUpEvent event) {
        return orderUpEmitter.send(jsonb.toJson(event)).toCompletableFuture();
    }

}
