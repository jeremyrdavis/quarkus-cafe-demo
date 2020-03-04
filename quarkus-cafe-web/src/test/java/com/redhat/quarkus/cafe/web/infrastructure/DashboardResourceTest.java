package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import com.redhat.quarkus.cafe.web.domain.Item;
import com.redhat.quarkus.cafe.web.domain.OrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class DashboardResourceTest {

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testHelloEndpoint() {

        DashboardUpdate dashboardUpdate = new DashboardUpdate();
        dashboardUpdate.item = Item.CAPPUCCINO;
        dashboardUpdate.itemId = UUID.randomUUID().toString();
        dashboardUpdate.name = "Mickey";
        dashboardUpdate.status = OrderStatus.IN_QUEUE;
        dashboardUpdate.orderId = UUID.randomUUID().toString();

        given()
                .when()
                .body(jsonb.toJson(dashboardUpdate).toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post("/update")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

}
