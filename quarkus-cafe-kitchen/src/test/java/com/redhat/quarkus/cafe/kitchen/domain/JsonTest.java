package com.redhat.quarkus.cafe.kitchen.domain;

import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    final String orderInJsonString = "{\"eventType\":\"KITCHEN_ORDER_IN\",\"item\":\"CROISSANT\",\"itemId\":\"d76331dc-a1dc-4042-9766-dd5a0b151022\",\"name\":\"Donald\",\"orderId\":\"063ae650-9d15-44b7-841d-c2d1a1c04e3c\"}";

    @Test
    public void testOrderInJson() {

        JsonReader reader = Json.createReader(new StringReader(orderInJsonString));
        JsonObject jsonObject = reader.readObject();
        String eventType = jsonObject.getString("eventType");

        assertEquals(EventType.KITCHEN_ORDER_IN.toString(), eventType);
    }
}
