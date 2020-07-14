package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.eclipse.microprofile.reactive.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.redhat.quarkus.cafe.infrastructure.JsonUtil.*;

@ApplicationScoped
public class KafkaService {

    final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Inject
    Cafe cafe;

    @Inject
    @Channel("barista-out")
    Emitter<String> baristaOutEmitter;

    @Inject
    @Channel("kitchen-out")
    Emitter<String> kitchenOutEmitter;

    @Inject
    @Channel("web-updates-out")
    Emitter<String> webUpdatesOutEmitter;

    @Incoming("web-in")
    public CompletionStage<Void> onOrderIn(final Message message) {
        logger.debug("orderIn: {}", message.getPayload());
        return handleCreateOrderCommand(createOrderCommandFromJson(message.getPayload().toString())).thenRun(()->{message.ack();});
    }

    protected CompletionStage<Void> handleCreateOrderCommand(final CreateOrderCommand createOrderCommand) {

        return CompletableFuture.supplyAsync(() -> {



           // Get the event from the Order domain object
            OrderCreatedEvent orderCreatedEvent = cafe.processCreateOrderCommand(createOrderCommand);

            orderCreatedEvent.getEvents().forEach(e -> {
                if (e.eventType.equals(EventType.BEVERAGE_ORDER_IN)) {
                    baristaOutEmitter.send(toJson(e))
                            .thenAccept(r -> {
                                logger.debug("barista-in event sent {}", e);
                                webUpdatesOutEmitter.send(toInProgressUpdate(e))
                                        .thenAccept(s -> {
                                            logger.debug("web update sent {}", r);
                                        })
                                        .exceptionally(ex -> {
                                            logger.error(ex.getMessage());
                                            throw new RuntimeException(ex);
                                        });
                            })
                            .exceptionally(ex -> {
                                logger.error(ex.getMessage());
                                throw new RuntimeException(ex);
                            });
                } else if (e.eventType.equals(EventType.KITCHEN_ORDER_IN)) {
                    kitchenOutEmitter.send(toJson(e))
                            .thenAccept(r -> {
                                logger.debug("kitchen-in event sent {}", e);
                                webUpdatesOutEmitter.send(toInProgressUpdate(e))
                                        .thenAccept(s -> {
                                            logger.debug("web update sent {}", r);
                                        })
                                        .exceptionally(ex -> {
                                            logger.error(ex.getMessage());
                                            throw new RuntimeException(ex);
                                        });
                            })
                            .exceptionally(ex -> {
                                logger.error(ex.getMessage());
                                throw new RuntimeException(ex);
                            });
                }
            });
            return null;
        });

    }

    @Incoming("orders-up")
    @Outgoing("web-updates-order-up")
    public String onOrderUp(String payload) {
        logger.debug("received order up {}", payload);
        return toDashboardUpdateReadyJson(payload);
    }
}
