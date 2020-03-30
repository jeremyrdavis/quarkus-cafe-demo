package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaTestUtil {


    @Inject
    @Channel("orders-out")
    Emitter<String> orderUpEmitter;

    public void send(String event) {
        orderUpEmitter.send(event);
    }
}
