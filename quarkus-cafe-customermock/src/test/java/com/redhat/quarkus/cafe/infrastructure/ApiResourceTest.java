package com.redhat.quarkus.cafe.infrastructure;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(ApiResource.class)
public class ApiResourceTest {

    @InjectMock
    @RestClient
    RESTService restService;

    @Test
    public void testStartApplication() {
        given()
                .when().post("/start")
                .then()
                .statusCode(200);

        given()
                .when().get("/running")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testStopApplication() {
        given()
                .when().post("/stop")
                .then()
                .statusCode(200);

        given()
                .when().get("/running")
                .then()
                .statusCode(200)
                .body(is("false"));
    }
}
