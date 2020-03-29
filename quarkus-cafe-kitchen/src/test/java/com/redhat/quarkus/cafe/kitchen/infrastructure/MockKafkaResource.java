package com.redhat.quarkus.cafe.kitchen.infrastructure;

import io.quarkus.test.Mock;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@Mock
@ApplicationScoped
public class MockKafkaResource extends KafkaResource{

    @Override
    public void orderIn(String message) {
        System.out.println("orderIn");
    }

}
