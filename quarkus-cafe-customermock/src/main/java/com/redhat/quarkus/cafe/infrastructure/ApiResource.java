package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CustomerMocker;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api")
public class ApiResource {

    final Logger logger = LoggerFactory.getLogger(ApiResource.class);

    @Inject
    CustomerMocker customerMocker;

    @POST
    @Path("/start")
    public Response startMocking() {
        logger.info("starting");
        customerMocker.start();
        return Response.ok().build();
    }

    @POST
    @Path("/stop")
    public Response stopMocking() {
        logger.info("stopping");
        customerMocker.stop();
        return Response.ok().build();
    }

    @GET
    @Path("/running")
    public Response isRunning() {
        logger.info("returning status {}", customerMocker.isRunning());
        return Response.ok(customerMocker.isRunning()).build();
    }

    @POST
    @Path("/dev")
    public Response setVolumeToDev() {
        logger.info("setting volume to Dev");
        customerMocker.setVolumeToDev();
        return Response.ok().build();
    }
}
