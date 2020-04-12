package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RegisterForReflection
@MongoEntity(collection = "Orders")
public class Order extends PanacheMongoEntity {

    public ObjectId id;

    public List<LineItem> beverageLineItems = new ArrayList<>();

    public List<LineItem> kitchenLineItems = new ArrayList<>();

    public List<LineItem> getBeverageLineItems() {
        return beverageLineItems;
    }

    public List<LineItem> getKitchenLineItems() {
        return kitchenLineItems;
    }
}
