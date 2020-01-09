package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.Beverage;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RestResourceTest {

    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/order")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testOrderIn() {

        List<Beverage> beverageList = new ArrayList<>();
        beverageList.add(new Beverage(Beverage.Type.CAPUCCINO));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(UUID.randomUUID().toString());
        createOrderCommand.addBeverages(beverageList);

        System.out.println(jsonb.toJson(createOrderCommand));
        given()
                .body(jsonb.toJson(createOrderCommand).toString())
                .contentType(ContentType.JSON)
                .when().post("/order")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

}