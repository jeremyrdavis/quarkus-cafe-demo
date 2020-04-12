package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

@RegisterForReflection
@MongoEntity
public class LineItem {

    public ObjectId orderId;

    public Item item;

    public String name;

    public LineItem(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    public LineItem() {
    }
}
