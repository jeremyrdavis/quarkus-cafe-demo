package com.redhat.quarkus.cafe.domain;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MongoEntity(collection = "Orders")
public class Order extends PanacheMongoEntity {

    public ObjectId id;

    public List<LineItem> beverageLineItems;

    public List<LineItem> kitchenLineItems;

    public void addBeverageLineItem(LineItem b) {
    }

    public List<LineItem> getBeverageLineItems() {
        if (beverageLineItems != null) {
            return beverageLineItems;
        }else {
            this.beverageLineItems = new ArrayList<>();
            return beverageLineItems;
        }
    }

    public List<LineItem> getKitchenLineItems() {
        if (kitchenLineItems != null) {
            return kitchenLineItems;
        }else {
            this.kitchenLineItems = new ArrayList<>();
            return kitchenLineItems;
        }
    }
}
