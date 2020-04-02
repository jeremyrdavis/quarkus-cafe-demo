package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@Path("/")
public class RestResource {

    Logger logger = LoggerFactory.getLogger(RestResource.class);

    @ConfigProperty(name="sourceUrl")
    String sourceUrl;

    @Inject
    OrderService orderService;

    @Inject
    Template cafeTemplate;

    Jsonb jsonb = JsonbBuilder.create();

    @GET
    @Path("/cafe")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIndex(){
        return cafeTemplate.data("sourceUrl", sourceUrl);
    }

    @POST
    @Path("/order")
    public Response orderIn(CreateOrderCommand createOrderCommand) {

        logger.debug("CreateOrderCommand received: {}", toJson(createOrderCommand));
        orderService.sendOrder(createOrderCommand);
        return Response.accepted().entity(createOrderCommand).build();
    }

    @POST
    @Path("/updates")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDashboard(List<DashboardUpdate> dashboardUpdates) {

        logger.debug("{} updates received", dashboardUpdates.size());
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
