package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.Beverage;
import com.redhat.quarkus.cafe.barista.domain.OrderInEvent;
import com.redhat.quarkus.cafe.barista.domain.Status;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiResource {

    @GET
    @Path("/orderInEvent")
    public Response orderInEvent() {

        OrderInEvent retVal = new OrderInEvent(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Jeremy", Beverage.BLACK_COFFEE, Status.IN_QUEUE);
        return Response.ok().entity(retVal).build();
    }
}