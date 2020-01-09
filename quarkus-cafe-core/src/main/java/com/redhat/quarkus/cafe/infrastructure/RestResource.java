package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.jboss.logging.Logger;
import org.locationtech.jts.operation.overlay.MaximalEdgeRing;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

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