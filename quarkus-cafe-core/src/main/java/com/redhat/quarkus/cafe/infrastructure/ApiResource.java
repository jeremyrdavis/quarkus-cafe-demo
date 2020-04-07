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
        createOrderCommand.addKitchenItems(createKitchenItems());
        return Response.ok().entity(createOrderCommand).build();
    }

    @GET
    @Path("/beverageOrderInEvent")
    public Response getOrderInEvent() {

        BeverageLineItemInEvent retVal = new BeverageLineItemInEvent(UUID.randomUUID().toString(),"Goofy", Item.ESPRESSO);
        return Response.ok().entity(retVal).build();
    }

    @GET
    @Path("/kitchenOrderInEvent")
    public Response getKitchenOrderInEvent(){

        KitchenLineItemInEvent retVal = new KitchenLineItemInEvent(UUID.randomUUID().toString(),"Goofy", Item.CAKEPOP);
        return Response.ok().entity(retVal).build();
    }

    @GET
    @Path("/beverageOrderUpEvent")
    public LineItemUpEvent getBeverageOrderUpEvent() {

        return new LineItemUpEvent(UUID.randomUUID().toString(), "Moe", Item.COFFEE_BLACK, EventType.BEVERAGE_ORDER_UP);
    }

    private List<LineItem> createBeverages() {

        List<LineItem> beverages = new ArrayList(2);
        beverages.add(new LineItem(Item.COFFEE_WITH_ROOM, "Mickey"));
        beverages.add(new LineItem(Item.COFFEE_BLACK, "Minnie"));
        return beverages;
    }

    private List<LineItem> createKitchenItems() {
        List<LineItem> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new LineItem(Item.CAKEPOP, "Mickey"));
        kitchenOrders.add(new LineItem(Item.CROISSANT, "Minnie"));
        return kitchenOrders;
    }


}
