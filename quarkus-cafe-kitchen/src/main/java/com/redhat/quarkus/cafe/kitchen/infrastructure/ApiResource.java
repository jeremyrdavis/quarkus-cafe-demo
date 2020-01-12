package com.redhat.quarkus.cafe.kitchen.infrastructure;

import com.redhat.quarkus.cafe.kitchen.domain.EventType;
import com.redhat.quarkus.cafe.kitchen.domain.Item;
import com.redhat.quarkus.cafe.kitchen.domain.OrderEvent;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    @Path("/orderInEvent")
    public Response getOrderInEvent() {

        OrderEvent retVal = new OrderEvent(
                UUID.randomUUID().toString(),
                "Goofy",
                Item.CAKEPOP,
                UUID.randomUUID().toString(),
                EventType.KITCHEN_ORDER_IN);

        return Response.ok().entity(retVal).build();

    }
}