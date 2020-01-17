package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.*;

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
    public Response orderInEvent() {

        BeverageOrder retVal = new BeverageOrder(EventType.BEVERAGE_ORDER_IN, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK);
        return Response.ok().entity(retVal).build();
    }

    @GET
    @Path("/orderUpEvent")
    public Response orderUpEvent() {

        BeverageOrder retVal = new BeverageOrder(EventType.BEVERAGE_ORDER_UP, UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Item.COFFEE_BLACK);
        return Response.ok().entity(retVal).build();
    }
}