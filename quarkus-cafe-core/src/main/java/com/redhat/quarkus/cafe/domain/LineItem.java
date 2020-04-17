package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;

@RegisterForReflection
@MongoEntity
public class LineItem {

    public ObjectId orderId;

    public Item item;

    public String name;

    public LineItem(ObjectId orderId, Item item, String name) {
        this.orderId = orderId;
        this.item = item;
        this.name = name;
    }

    public LineItem(Item item, String name) {
        this.item = item;
        this.name = name;
    }

    public LineItem() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orderId", orderId)
                .append("item", item)
                .append("name", name).toString();
    }
}
