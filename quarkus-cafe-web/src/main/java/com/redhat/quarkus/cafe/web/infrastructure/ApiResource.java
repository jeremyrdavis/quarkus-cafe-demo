package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import com.redhat.quarkus.cafe.web.domain.Item;
import com.redhat.quarkus.cafe.web.domain.OrderStatus;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

    Logger logger = Logger.getLogger(ApiResource.class);

    @GET
    @Path("/update")
    public Response getCreateOrderCommandJson() {
        DashboardUpdate dashboardUpdate = new DashboardUpdate(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Jeremy",
                Item.CROISSANT,
                OrderStatus.IN_QUEUE);
        return Response.ok().entity(dashboardUpdate).build();
    }
}