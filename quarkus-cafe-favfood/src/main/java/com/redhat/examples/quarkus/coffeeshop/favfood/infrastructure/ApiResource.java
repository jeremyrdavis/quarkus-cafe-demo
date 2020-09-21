package com.redhat.examples.quarkus.coffeeshop.favfood.infrastructure;

import com.redhat.examples.quarkus.coffeeshop.favfood.domain.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource extends BaseResource{

    @POST
    public Order favFoodDeliveryOrder(){
        return mockOrder();
    }
}
