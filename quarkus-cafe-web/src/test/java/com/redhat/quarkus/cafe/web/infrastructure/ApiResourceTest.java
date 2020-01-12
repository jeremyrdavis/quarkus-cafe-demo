package com.redhat.quarkus.cafe.web.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ApiResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/api/update")
          .then()
             .statusCode(200)
                .contentType(ContentType.JSON);
    }

}