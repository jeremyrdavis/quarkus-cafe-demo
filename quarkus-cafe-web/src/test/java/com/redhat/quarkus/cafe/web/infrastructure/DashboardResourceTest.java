package com.redhat.quarkus.cafe.web.infrastructure;

import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.OrderStatus;
import com.redhat.quarkus.cafe.web.domain.DashboardUpdate;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.util.ArrayList;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class DashboardResourceTest extends KafkaResourceIT{

    Logger logger = LoggerFactory.getLogger(DashboardResourceTest.class);

    Jsonb jsonb = JsonbBuilder.create();

    WebTarget target;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092");
    }

    @Test
    public void testHelloEndpoint() {

        ArrayList<InboundSseEvent> events = new ArrayList<>();

        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8081/dashboard/stream");
        SseEventSource msgEventSource = SseEventSource.target(target).build();
        msgEventSource.register(event -> {
            logger.info(event.readData());
            events.add(event);
        });
        msgEventSource.open();
        assertTrue(msgEventSource.isOpen());
/*
        try (SseEventSource eventSource = msgEventSource) {
            eventSource.register(event -> {
                System.out.println(event.readData(String.class));
            }, ex -> {
                ex.printStackTrace();
            });
            eventSource.open();
        }
*/

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
                .statusCode(200);

        assertEquals(1, events.size());

    }
}
