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
import java.util.stream.Collectors;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {

    @GET
    @Path("/order")
    public Response orderJson() {

        return Response.ok(mockOrder()).build();
    }

    private Order mockOrder() {

        return Order.createOrder("Paul", mockLineItems());
    }

    private List<LineItem> mockLineItems() {
        int lineItemsCount = new Random().nextInt(6);
        List<LineItem> lineItems = new ArrayList<LineItem>(lineItemsCount);
        for (int i = 0; i <= lineItemsCount; i++) {
            lineItems.add(LineItem.createLineItem("BLACK_COFFEE", 1));
        }
        return lineItems;
    }
}