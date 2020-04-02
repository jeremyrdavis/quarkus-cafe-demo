package com.redhat.quarkus.cafe.infrastructure;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonUtil {

    static final Jsonb jsonb = JsonbBuilder.create();

    public static String toJson(Object object) {
        return jsonb.toJson(object);
    }
}
