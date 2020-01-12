package com.redhat.quarkus.cafe.kitchen.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ApiResourceTest {

    @Test
    public void testOrderInEvent() {
        given()
          .when().get("/api/orderInEvent")
          .then()
             .statusCode(200);
    }

}