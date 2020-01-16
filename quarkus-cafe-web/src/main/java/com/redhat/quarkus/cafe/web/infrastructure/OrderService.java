package com.redhat.quarkus.cafe.web.infrastructure;


import com.redhat.quarkus.cafe.web.domain.CreateOrderCommand;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/order")
@RegisterRestClient
public interface OrderService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void orderIn(CreateOrderCommand createOrderCommand);
}
