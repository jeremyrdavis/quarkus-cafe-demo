package com.redhat.quarkus.cafe.web.infrastructure;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@RegisterForReflection
public class JsonUtil {

    static final Jsonb jsonb = JsonbBuilder.create();

    static String toJson(final Object object) {
        return jsonb.toJson(object);
    }

}
