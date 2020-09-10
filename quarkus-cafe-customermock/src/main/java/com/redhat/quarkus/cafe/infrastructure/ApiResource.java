package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CustomerMocker;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mockito.Mockito;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api")
public class ApiResource {

    @Inject
    CustomerMocker customerMocker;

    @POST
    @Path("/start")
    public Response startMocking() {
        customerMocker.start();
        return Response.ok().build();
    }

    @POST
    @Path("/stop")
    public Response stopMocking() {
        customerMocker.stop();
        return Response.ok().build();
    }

    @GET
    @Path("/running")
    public Response isRunning() {
        return Response.ok(customerMocker.isRunning()).build();
    }
}
