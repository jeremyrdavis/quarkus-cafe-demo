package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.DashboardUpdate;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/update")
@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DashboardService {


    @POST
    public void updatedDashboard(List<DashboardUpdate> dashboardUpdate);



}
