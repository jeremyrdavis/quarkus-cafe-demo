package com.redhat.quarkus.cafe.infrastructure;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ApiResourceTest {

    @Test
    public void testStartApplication() {
        given()
                .when().post("/api/start")
                .then()
                .statusCode(200);

        given()
                .when().get("/api/running")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testStopApplication() {
        given()
                .when().post("/api/stop")
                .then()
                .statusCode(200);

        given()
                .when().get("/api/running")
                .then()
                .statusCode(200)
                .body(is("false"));
    }
}
