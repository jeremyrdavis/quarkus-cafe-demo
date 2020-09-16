package com.redhat.demos.quarkus.coffeeshop.inventory.infrastructure;

import com.redhat.demos.quarkus.coffeeshop.inventory.domain.CoffeeshopCommand;
import com.redhat.demos.quarkus.coffeeshop.inventory.domain.CommandType;
import com.redhat.demos.quarkus.coffeeshop.inventory.domain.RestockItemCommand;
import com.redhat.demos.quarkus.coffeeshop.inventory.domain.StockRoom;
import com.redhat.quarkus.cafe.domain.Event;
import com.redhat.quarkus.cafe.domain.EventType;
import com.redhat.quarkus.cafe.domain.OrderInEvent;
import org.eclipse.microprofile.reactive.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonBuilderFactory;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    private Jsonb jsonb = JsonbBuilder.create();

    @Inject
    StockRoom stockRoom;

    @Inject
    @Channel("inventory-out")
    Emitter<String> inventoryEmitter;

    @Incoming("inventory-in")
    public CompletionStage<Void> processRestockCommand(final Message message) {
        logger.debug("\nRestockItemCommand Received: {}", message.getPayload().toString());
        final RestockItemCommand restockItemCommand = jsonb.fromJson((String) message.getPayload(), RestockItemCommand.class);
        if (restockItemCommand.commandType.equals(CommandType.RESTOCK_INVENTORY_COMMAND)) {
            return stockRoom.handleRestockItemCommand(restockItemCommand.getItem())
                    .thenApply(c -> {
                        return sendCommand(c);
                    }).thenRun(() -> {
                        message.ack();
                    });
        }else{
            return message.ack();
        }
    }
/*
    @Incoming("inventory-in")
    public CompletionStage<Void> handleRestockCommand(Message message) {
        logger.debug("\nRestockItemCommand received: {}", message.getPayload());
        final RestockItemCommand restockItemCommand = (RestockItemCommand) message.getPayload();
        if (restockItemCommand.commandType.equals(CommandType.RESTOCK_INVENTORY_COMMAND)) {
            return stockRoom.handleRestockItemCommand(restockItemCommand.getItem()).thenApply(c -> {
                return sendCommand(c);
            }).thenRun(() -> {
                message.ack();
            });
        } else {
            return message.ack();
        }
    }

*/
    CompletableFuture<Void> sendCommand(final CoffeeshopCommand coffeeshopCommand) {
        logger.debug("sending: {}", coffeeshopCommand.toString());
        return inventoryEmitter.send(jsonb.toJson(coffeeshopCommand))
                .toCompletableFuture();
    }


}
