package com.redhat.quarkus.cafe.web.infrastructure;


import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.concurrent.CompletionStage;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@RegisterForReflection
@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    public CompletionStage<Void> placeOrder(CreateOrderCommand createOrderCommand){
        return ordersOutEmitter.send(toJson(createOrderCommand))
            .whenCompleteAsync((result, ex) -> {
                logger.debug("createOrderCommand sent");
                logger.debug(ex.getMessage());
            });
    }
}
