package com.redhat.quarkus.cafe.domain;

import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonTest {


    static final String BEVERAGE_UP = "{\"eventType\":\"BEVERAGE_ORDER_UP\",\"item\":\"COFFEE_BLACK\",\"itemId\":\"e6763a66-6eba-451b-9d8b-dc300aeb0d3a\",\"name\":\"Jeremy\",\"orderId\":\"290e5989-2136-4eac-b792-e8fd1cf4bb02\"}";

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testMarshallingJson() {

        LineItemEvent result = jsonb.fromJson(BEVERAGE_UP, LineItemEvent.class);
        assertNotNull(result);
        assertEquals(Item.COFFEE_BLACK, result.item);
        assertEquals(EventType.BEVERAGE_ORDER_UP, result.eventType);
        assertEquals("e6763a66-6eba-451b-9d8b-dc300aeb0d3a", result.itemId);
    }
}
