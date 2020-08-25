package com.redhat.examples;

import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.LineItem;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class JSONResourceTest {


    @Test
    public void testHelloEndpoint() {

        when().get("/json/CreateOrderCommand")
                .then().body("beverages.item", hasItems(Item.ESPRESSO.name(), Item.COFFEE_BLACK.name()));
        when().get("/json/CreateOrderCommand")
                .then().body("kitchenOrders.item", hasItems(Item.CAKEPOP.name(), Item.CROISSANT.name()));
        when().get("/json/CreateOrderCommand")
                .then().body("id", notNullValue());
    }

}
