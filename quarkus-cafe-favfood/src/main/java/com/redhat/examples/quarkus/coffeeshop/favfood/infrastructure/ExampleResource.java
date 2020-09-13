package com.redhat.examples.quarkus.coffeeshop.favfood.infrastructure;

import com.redhat.examples.quarkus.coffeeshop.favfood.domain.LineItem;
import com.redhat.examples.quarkus.coffeeshop.favfood.domain.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {

    @GET
    @Path("/order")
    public Response orderJson() {

        return Response.accepted(mockOrder()).build();
    }

    private Order mockOrder() {

        return Order.createOrder(randomName(), mockLineItems());
    }

    private List<LineItem> mockLineItems() {
        int lineItemsCount = new Random().nextInt(6);
        List<LineItem> lineItems = new ArrayList<LineItem>(lineItemsCount);
        for (int i = 0; i <= lineItemsCount; i++) {
            lineItems.add(LineItem.createLineItem(randomBeverage(), 1));
            if (i % 3 == 0) {
                lineItems.add(LineItem.createLineItem(randomFood(), 1));
            }
        }
        return lineItems;
    }

    private String randomName() {
        return randomString(new String[]{"John", "Paul", "George", "Ringo", "Dolly", "Rosie", "Carmen", "Kim", "Thurston", "Lee", "Steve"});
    }

    private String randomFood() {
        return randomString( new String[]{"CAKEPOP", "CROISSANT", "MUFFIN", "CROISSANT_CHOCOLATE"});
    }

    String randomBeverage() {
        return randomString( new String[]{"BLACK_COFFEE", "COFFEE_WITH_ROOM", "ESPRESSO", "DOUBLE_ESPRESSO", "LATTE"});
    }

    String randomString(String[] strings) {
        int pos = new Random().nextInt(strings.length);
        if(pos > 0) pos--;
        return strings[pos];
    }
}