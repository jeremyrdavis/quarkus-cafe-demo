package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestResource {

    Logger logger = Logger.getLogger(RestResource.class);

    @Inject
    Cafe cafe;

    Jsonb jsonb = JsonbBuilder.create();

    @GET
    public String hello() {
        return "hello";
    }

    @POST
    public CompletionStage<Response> orderIn(CreateOrderCommand createOrderCommand) {

        logger.debug(createOrderCommand);

        final CompletableFuture<Response> response = new CompletableFuture<>();

        cafe.orderIn(createOrderCommand).thenApply(res -> {
            Response jaxrs = Response.accepted().entity(res).build();
            response.complete(jaxrs);
            return null;
        });
        return response;
    }

}