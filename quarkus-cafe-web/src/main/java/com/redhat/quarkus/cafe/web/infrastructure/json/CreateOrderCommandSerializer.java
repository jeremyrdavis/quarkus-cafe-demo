package com.redhat.quarkus.cafe.web.infrastructure.json;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Utility class for serializing our domain object to JSON
 */
@RegisterForReflection
public class CreateOrderCommandSerializer extends JsonbDeserializer<CreateOrderCommand> {

    public CreateOrderCommandSerializer() {
        super(CreateOrderCommand.class);
    }
}
