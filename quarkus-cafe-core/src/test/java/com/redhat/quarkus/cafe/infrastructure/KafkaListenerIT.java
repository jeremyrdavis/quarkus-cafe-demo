package com.redhat.quarkus.cafe.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@QuarkusTest @Testcontainers
public class KafkaListenerIT extends BaseTestContainersIT{

    @Test
    public void testBeverageOrderUp() {

    }
}
