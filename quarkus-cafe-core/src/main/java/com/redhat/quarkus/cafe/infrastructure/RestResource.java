package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestResource {

    Logger logger = Logger.getLogger(RestResource.class);

    @Inject
    CafeCore cafeCore;

    Jsonb jsonb = JsonbBuilder.create();

    @GET
    public String hello() {
        return "hello";
    }

    @POST
    public CompletionStage<Response> orderIn(CreateOrderCommand createOrderCommand) {

        logger.debug(createOrderCommand);

        return CompletableFuture.supplyAsync(() -> {
            try {
                List<OrderEvent> allOrders = cafeCore.orderIn(createOrderCommand);
                return Response.accepted().entity(allOrders).build();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Response.serverError().entity(e).build();
            } catch (ExecutionException e) {
                e.printStackTrace();
                return Response.serverError().entity(e).build();
            }
        });
/*
        cafe.orderIn(createOrderCommand).thenApply(res -> {
            Response jaxrs = Response.accepted().entity(res).build();
            return response.complete(jaxrs);
        });
*/
    }

}