package com.redhat.quarkus.cafe.web.infrastructure.json;

import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

/**
 * Utility class for serializing our domain object to JSON
 */
public class CreateOrderCommandSerializer extends JsonbDeserializer<CreateOrderCommand> {

    public CreateOrderCommandSerializer() {
        super(CreateOrderCommand.class);
    }
}
