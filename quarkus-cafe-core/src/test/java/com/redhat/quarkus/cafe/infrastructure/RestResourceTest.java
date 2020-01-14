package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.Order;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

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

    @Test @Timeout(5)
    public void testOrderIn() {

        List<Order> beverageList = new ArrayList<>();
        beverageList.add(new Order(Item.CAPPUCCINO, "Huey"));

        CreateOrderCommand createOrderCommand = new CreateOrderCommand();
        createOrderCommand.addBeverages(beverageList);

        System.out.println(jsonb.toJson(createOrderCommand));
        given()
                .body(jsonb.toJson(createOrderCommand))
                .contentType(ContentType.JSON)
                .when().post("/order")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

}