package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import kafka.security.auth.Create;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonUtil {

    static final Jsonb jsonb = JsonbBuilder.create();

    public static String toJson(Object object) {
        return jsonb.toJson(object);
    }

    public static CreateOrderCommand createOrderCommandFromJson(String payload) {
        return jsonb.fromJson(payload, CreateOrderCommand.class);
    }
}
