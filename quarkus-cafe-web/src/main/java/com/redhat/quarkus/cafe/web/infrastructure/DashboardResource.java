package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
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

@Path("/dashboard")
public class DashboardResource {

    @Inject @Channel("dashboard")
    Publisher<String> updates;

    @Inject @Channel("next")
    Publisher<String> updatesInternal;

    @Inject @Channel("post")
    Emitter<String> updateEmitter;

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
/*
        Arrays.asList(dashboardUpdates).forEach(dashboardUpdate -> {
            System.out.println(dashboardUpdate + "\n");
            updateEmitter.send(jsonb.toJson(dashboardUpdate).toString());
        });
*/
        return Response.ok().build();
    }

}
