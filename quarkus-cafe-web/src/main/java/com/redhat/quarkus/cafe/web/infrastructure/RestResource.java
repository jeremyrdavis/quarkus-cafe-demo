package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class RestResource {

    Logger logger = LoggerFactory.getLogger(RestResource.class);

    @Inject
    @Channel("dashboard")
    Emitter<String> udpateEmitter;

    @Inject
    @RestClient
    OrderService orderService;

    Jsonb jsonb = JsonbBuilder.create();

    @POST
    @Path("/order")
    public Response orderIn(CreateOrderCommand createOrderCommand) {

        System.out.println("\norder in\n");
        System.out.println("\n"+ createOrderCommand +"\n");
        orderService.orderIn(createOrderCommand);
        System.out.println("\nsent\n");
        return Response.accepted().entity(createOrderCommand).build();
    }

    @POST
    @Path("/updates")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDashboard(List<DashboardUpdate> dashboardUpdates) {

        logger.debug("{} updates received", dashboardUpdates.size());
        udpateEmitter.send(jsonb.toJson(dashboardUpdates.get(0)));
        dashboardUpdates.forEach( dashboardUpdate -> {
//            udpateEmitter.send(jsonb.toJson(dashboardUpdate));
            logger.debug("update sent {}", dashboardUpdate);
        });
        return Response.ok().build();
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response singleUpdate(DashboardUpdate dashboardUpdate) {

        logger.debug("update received {}", dashboardUpdate);
        try {

//            udpateEmitter.send(jsonb.toJson(dashboardUpdate));
            logger.debug("update sent {}", dashboardUpdate);
        } catch (Exception e) {
            logger.error("Emitter error {}", e.getMessage());
            logger.error("update failed to send");
            e.printStackTrace();
            return Response.serverError().entity(e).build();
        }
        return Response.ok().build();
    }

}
