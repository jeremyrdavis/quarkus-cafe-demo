package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.Beverage;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest @Testcontainers
public class RestResourceIT extends BaseTestContainersIT{

    @Test
    @Timeout(60)
    public void testSendKitchenOrder() throws ExecutionException, InterruptedException {

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

        ConsumerRecords<String, String> newRecords = kafkaConsumer.poll(Duration.ofMillis(10000));
        assertNotNull(newRecords);
        assertEquals(1, newRecords.count());
        for (ConsumerRecord<String, String> record : newRecords) {
            System.out.printf("offset = %d, key = %s, value = %s\n",
                    record.offset(),
                    record.key(),
                    record.value());
        }
    }

}
