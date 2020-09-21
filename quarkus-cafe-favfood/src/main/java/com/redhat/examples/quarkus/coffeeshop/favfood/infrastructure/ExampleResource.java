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

@Path("/json")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource extends BaseResource {

    @GET
    @Path("/order")
    public Response orderJson() {

        return Response.accepted(mockOrder()).build();
    }

}