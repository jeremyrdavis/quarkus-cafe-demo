package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
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
import java.util.concurrent.CompletionStage;

import static com.redhat.quarkus.cafe.web.infrastructure.JsonUtil.toJson;

@RegisterForReflection
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
        return orderService.placeOrder(createOrderCommand)
            .handle((res, ex) -> {
                if (ex != null) {
                    logger.error(ex.getMessage());
                    return Response.serverError().entity(ex).build();
                }else{
                    return Response.accepted().entity(createOrderCommand).build();
                }
            }).join();
    }

}
