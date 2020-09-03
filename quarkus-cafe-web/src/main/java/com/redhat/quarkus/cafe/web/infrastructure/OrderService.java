package com.redhat.quarkus.cafe.web.infrastructure;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.redhat.quarkus.cafe.domain.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@RegisterForReflection
@ApplicationScoped
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject
    @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    public CompletableFuture<Void> placeOrder(final OrderInCommand orderInCommand){
        return ordersOutEmitter.send(toJson(orderInCommand))
            .whenComplete((result, ex) -> {
                logger.debug("orderInCommand sent");
                if (ex != null) {
                    logger.error(ex.getMessage());
                }
            }).toCompletableFuture();
    }
}
