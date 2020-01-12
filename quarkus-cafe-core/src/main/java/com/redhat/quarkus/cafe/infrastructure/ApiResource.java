package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.jboss.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    Logger logger = Logger.getLogger(ApiResource.class);

    @GET
    @Path("/createOrderCommand")
    public Response getCreateOrderCommandJson() {
        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
        createOrderCommand.addBeverages(createBeverages());
        return Response.ok().entity(createOrderCommand).build();
    }

    @GET
    @Path("/beverageOrderInEvent")
    public Response getOrderInEvent() {

        BeverageOrderInEvent retVal = new BeverageOrderInEvent(UUID.randomUUID().toString(),"Goofy", Item.ESPRESSO);
        return Response.ok().entity(retVal).build();
    }

    @GET
    @Path("/kitchenOrderInEvent")
    public Response getKitchenOrderInEvent(){

        KitchenOrderInEvent retVal = new KitchenOrderInEvent(UUID.randomUUID().toString(),"Goofy", Item.CAKEPOP);
        return Response.ok().entity(retVal).build();
    }

    private List<Order> createBeverages() {
        List<Order> beverages = new ArrayList(2);
        beverages.add(new Order(Item.COFFEE_WITH_ROOM, "Mickey"));
        beverages.add(new Order(Item.COFFEE_BLACK, "Minnie"));
        return beverages;
    }

}
