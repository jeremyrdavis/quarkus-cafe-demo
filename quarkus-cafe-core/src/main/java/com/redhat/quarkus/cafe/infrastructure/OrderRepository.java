package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.Order;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheMongoRepository<Order> {
}
