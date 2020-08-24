package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@RegisterForReflection
public class JsonUtil {

    static final Jsonb jsonb = JsonbBuilder.create();

    public static String toJson(Object object) {
        return jsonb.toJson(object);
    }

    public static String toDashboardUpdateReadyJson(String payload) {
        OrderUpEvent orderUpEvent = jsonb.fromJson(payload, OrderUpEvent.class);
        return jsonb.toJson(new OrderReadyUpdate(orderUpEvent));
    }

    public static CreateOrderCommand createOrderCommandFromJson(String payload) {
        return jsonb.fromJson(payload, CreateOrderCommand.class);
    }

    public static String toInProgressUpdate(final LineItemEvent lineItemEvent) {
        return jsonb.toJson(new InQueueUpdate(lineItemEvent));
    }

}
