package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RegisterForReflection
public class Order {

    @BsonId
    public Long id;

    public List<LineItem> beverageLineItems = new ArrayList<>();

    public List<LineItem> kitchenLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(List<LineItem> beverageLineItems) {
        this.beverageLineItems = beverageLineItems;
    }

    public List<LineItem> getBeverageLineItems() {
        return beverageLineItems;
    }

    public List<LineItem> getKitchenLineItems() {
        return kitchenLineItems;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id:", this.id)
                .append("beverageLineItems", beverageLineItems.toString())
                .append("kitchenLineItems", kitchenLineItems.toString()).toString();
    }
}
