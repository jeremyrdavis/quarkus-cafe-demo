package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.OrderEvent;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonUtil {

    static final Jsonb jsonb = JsonbBuilder.create();

    static String toJson(final Object object) {
        return jsonb.toJson(object);
    }

    static OrderEvent orderEventFromJson(final String string) {
        return jsonb.fromJson(string, OrderEvent.class);
    }
}
