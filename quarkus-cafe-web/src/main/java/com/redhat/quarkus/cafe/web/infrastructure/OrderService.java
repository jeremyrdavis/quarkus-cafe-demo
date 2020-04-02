package com.redhat.quarkus.cafe.web.infrastructure;


import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@ApplicationScoped
public class OrderService {

    @Inject
    @Channel("orders-out")
    Emitter<String> ordersOutEmitter;

    public void sendOrder(CreateOrderCommand createOrderCommand){

        ordersOutEmitter.send(toJson(createOrderCommand));
    }
}
