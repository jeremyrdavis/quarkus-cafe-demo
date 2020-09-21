package com.redhat.examples.quarkus.coffeeshop.favfood.infrastructure;

import com.redhat.examples.quarkus.coffeeshop.favfood.domain.LineItem;
import com.redhat.examples.quarkus.coffeeshop.favfood.domain.Order;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseResource {
    protected Order mockOrder() {

        return Order.createOrder(randomName(), mockLineItems());
    }

    private List<LineItem> mockLineItems() {
        int lineItemsCount = new Random().nextInt(6);
        List<LineItem> lineItems = new ArrayList<LineItem>(lineItemsCount);
        for (int i = 0; i <= lineItemsCount; i++) {
            lineItems.add(LineItem.createLineItem(randomBeverage(), 1));
            if (i % 3 == 0) {
                lineItems.add(LineItem.createLineItem(randomFood(), 1));
            }
        }
        return lineItems;
    }

    String randomName() {
        return randomString(new String[]{"John", "Paul", "George", "Ringo", "Dolly", "Rosie", "Carmen", "Kim", "Thurston", "Lee", "Steve"});
    }

    String randomFood() {
        return randomString( new String[]{"CAKEPOP", "CROISSANT", "MUFFIN", "CROISSANT_CHOCOLATE"});
    }

    String randomBeverage() {
        return randomString( new String[]{"BLACK_COFFEE", "COFFEE_WITH_ROOM", "ESPRESSO", "DOUBLE_ESPRESSO", "LATTE"});
    }

    String randomString(String[] strings) {
        int pos = new Random().nextInt(strings.length);
        if(pos > 0) pos--;
        return strings[pos];
    }

    JsonObject mockOrderJson() {
        JsonObject jsonObject = Json.createObjectBuilder().build();
        jsonObject.se
    }
}
