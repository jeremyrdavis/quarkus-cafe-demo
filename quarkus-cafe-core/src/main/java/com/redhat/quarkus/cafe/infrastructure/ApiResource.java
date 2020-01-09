package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.Beverage;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
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
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString());
        createOrderCommand.addBeverages(createBeverages());
        return Response.ok().entity(createOrderCommand).build();
    }

    private List<Beverage> createBeverages() {
        List<Beverage> beverages = new ArrayList(2);
        beverages.add(new Beverage(Beverage.Type.CAPUCCINO));
        beverages.add(new Beverage(Beverage.Type.COFFEE_BLACK));
        return beverages;
    }

    @GET
    @Path("/beverage")
    public Beverage getBeverageJson() {
        return new Beverage(Beverage.Type.COFFEE_BLACK);
    }
}
