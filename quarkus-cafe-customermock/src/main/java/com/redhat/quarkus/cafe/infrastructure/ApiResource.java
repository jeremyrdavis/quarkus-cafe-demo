package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    @Path("/createOrderCommand")
    public Response getCreateOrderCommandJson() {
        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
        createOrderCommand.addBeverages(createBeverages());
        createOrderCommand.addKitchenItems(createKitchenItems());
        return Response.ok().entity(createOrderCommand).build();
    }

    private List<Order> createBeverages() {

        List<Order> beverages = new ArrayList(2);
        beverages.add(new Order(Beverage.COFFEE_WITH_ROOM, "Mickey"));
        beverages.add(new Order(Beverage.COFFEE_BLACK, "Minnie"));
        return beverages;
    }

    private List<Order> createKitchenItems() {
        List<Order> kitchenOrders = new ArrayList(2);
        kitchenOrders.add(new Order(Food.CAKEPOP, "Mickey"));
        kitchenOrders.add(new Order(Food.CROISSANT, "Minnie"));
        return kitchenOrders;
    }
}