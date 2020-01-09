package com.redhat.quarkus.cafe.domain;

import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonbTest {

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testCafeEventJson(){

        List<Beverage> beverageList = new ArrayList<>();
        beverageList.add(new Beverage(Beverage.Type.CAPUCCINO));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString());
        createOrderCommand.addBeverages(beverageList);

        CafeEvent cafeEvent = new BeverageOrderInEvent(UUID.randomUUID().toString(), new Beverage(Beverage.Type.CAPUCCINO));

        System.out.println(jsonb.toJson(cafeEvent));
    };
}
