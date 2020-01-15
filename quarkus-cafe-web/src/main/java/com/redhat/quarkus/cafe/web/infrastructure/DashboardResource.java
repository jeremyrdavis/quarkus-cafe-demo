package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.vertx.core.eventbus.EventBus;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Path("/dashboard")
public class DashboardResource {

    @Inject @Channel("dashboard")
    Publisher<String> updates;

    @Inject @Channel("next")
    Publisher<String> updatesInternal;

    @Inject @Channel("post")
    Emitter<String> updateEmitter;

    @Inject
    EventBus eventBus;

    Jsonb jsonb = JsonbBuilder.create();


    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<String> stream() {
        return Flowable.merge(updates, updatesInternal)
                .doOnSubscribe(s -> System.out.println("subscribing"))
                .doOnNext(s -> System.out.println("Got " + s));
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDashboard(List<DashboardUpdate> dashboardUpdates) {
        System.out.println("updates received");
        dashboardUpdates.forEach(d -> { System.out.println(d.toString() + "\n"); });

        try {
            return CompletableFuture.supplyAsync(() -> {
                dashboardUpdates.forEach(dashboardUpdate -> {
                    updateEmitter.send(jsonb.toJson(dashboardUpdate));
                });
                return Response.ok().build();
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
/*
        Arrays.asList(dashboardUpdates).forEach(dashboardUpdate -> {
            System.out.println(dashboardUpdate + "\n");
            updateEmitter.send(jsonb.toJson(dashboardUpdate).toString());
        });
*/
//        return Response.ok().build();
    }

}
