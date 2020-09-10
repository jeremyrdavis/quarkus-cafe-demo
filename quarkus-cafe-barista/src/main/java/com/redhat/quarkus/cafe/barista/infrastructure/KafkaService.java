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
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@ApplicationScoped
@RegisterForReflection
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    Barista barista;

    @Inject
    @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    private Jsonb jsonb = JsonbBuilder.create();

    @Incoming("orders-in")
    public CompletionStage<Void> handleOrderIn(Message message) {

        logger.debug("\nBarista Order In Received: {}", message.getPayload());
        final OrderInEvent orderIn = jsonb.fromJson((String) message.getPayload(), OrderInEvent.class);
        if (orderIn.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
            return barista.make(orderIn).thenApply(o -> {
                return sendEvents(o);
            }).thenRun(() -> {
                message.ack();
            });
        } else {
            return message.ack();
        }
    }

    CompletableFuture<Void> sendEvents(Collection<Event> events) {
        logger.debug("{} events returned", events.size());
        if (events.size() == 1) {
            return sendEvent((Event) events.toArray()[0]);
        }
        return CompletableFuture.allOf(
                events.stream().map(e -> {
                    return sendEvent(e);
                }).collect(Collectors.toList()).toArray(CompletableFuture[]::new))
                .exceptionally(e -> {
                    logger.error(e.getMessage());
                    return null;
                });
    }

    CompletableFuture<Void> sendEvent(final Event event) {
        logger.debug("sending: {}", event.toString());
        return orderUpEmitter.send(jsonb.toJson(event)).toCompletableFuture();
    }

}
