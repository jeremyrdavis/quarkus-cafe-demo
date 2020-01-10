package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.Update;
import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public void updateDashboard(Update update) {
        System.out.println("POST: " + jsonb.toJson(update).toString());
        updateEmitter.send(jsonb.toJson(update).toString());
    }


}
