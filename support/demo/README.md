# FavFood Integration Demo

## http://code.quarkus.io

Group: com.redhat.demos.quarkus.coffeeshop
Artifact: quarkus-coffeeshop-delivery
Build Tool: Maven

Select
RESTEasy JSON-B
SmallRye OpenAPI
SmallRye Reactive Messaging - Kafka Connector

Push to Github
git clone <<GIT_URL>>

## Open your favorite IDE

### Run tests

#### Run the existing test

#### Create a new Test Case:

```java
package com.redhat.demos.quarkus.coffeeshop.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FavFoodIntegrationTest {

    @Test
    public void testPostFavFoodOrder() {
        given()
                .when().post("/api/favfood")
                .then()
                .statusCode(202);
    }

}
```

Run the test, which should, of course, fail.


## Fire up Quarkus in dev mode:
```shell script
./mvnw clean compile quarkus:dev
```

The openapi specification supplied by FavFood is:
```yaml
---
openapi: 3.0.1
info:
  title: FavFood Integration API
  version: "1.0"
paths:
  /api/favFoodOrder:
    get:
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Order'
components:
  schemas:
    LineItem:
      type: object
      properties:
        item:
          type: string
        itemId:
          type: string
        quantity:
          format: int32
          type: integer
    ListLineItem:
      type: array
      items:
        $ref: '#/components/schemas/LineItem'
    Order:
      type: object
      properties:
        customerName:
          type: string
        id:
          type: string
        lineItems:
          $ref: '#/components/schemas/ListLineItem'

```

### Create the domain objects

Create a package in src/main/java: com.redhat.demos.quarkus.coffeeshop.domain.favfood

Create two Java classes, Order.java and LineItem, to model the openapi document that FavFood supplied:

```java
package com.redhat.demos.quarkus.coffeeshop.domain.favfood;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
public class Order {

    String id;

    static final String origin = "FavFood";

    String customerName;

    List<LineItem> lineItems;

    public static Order createOrder(String customerName, List<LineItem> lineItems) {
        Order favFoodOrder = new Order();
        favFoodOrder.customerName = customerName;
        favFoodOrder.lineItems = lineItems;
        return favFoodOrder;
    }

    public Order() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("customerName='" + customerName + "'")
                .add("lineItems=" + lineItems)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(lineItems, that.lineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, lineItems);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getOrigin() {
        return origin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}

```

and 

```java

package com.redhat.demos.quarkus.coffeeshop.domain.favfood;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@RegisterForReflection
public class LineItem {

    String itemId;

    String item;

    int quantity;

    public static LineItem createLineItem(String item, int quantity) {
        LineItem lineItem = new LineItem();
        lineItem.itemId = UUID.randomUUID().toString();
        lineItem.item = item;
        lineItem.quantity = quantity;
        return lineItem;
    }

    public LineItem() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LineItem.class.getSimpleName() + "[", "]")
                .add("itemId='" + itemId + "'")
                .add("item='" + item + "'")
                .add("quantity=" + quantity)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem that = (LineItem) o;
        return quantity == that.quantity &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, item, quantity);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
```

### Create a REST endpoint

Create a new packages: com.redhat.demos.quarkus.coffeeshop.infrastructure

Create a REST endpoint, FavFoodResource.java, in com.redhat.quarkus.demos.coffeeshop.infrastructure:

```java
package com.redhat.demos.quarkus.coffeeshop.infrastructure;

import com.redhat.demos.quarkus.coffeeshop.domain.favfood.FavFoodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api/favfood")
public class FavFoodResource {

    Logger logger = LoggerFactory.getLogger(FavFoodResource.class);

    @POST
    public Response processFavFoodOrder(Order favFoodOrder) {
        logger.debug("received {}", favFoodOrder);
        return Response.accepted().build();
    }
}
```

Set logging by opening src/main/resources/application.properties and adding the following:

```properties
# Logging
quarkus.log.console.enable=true
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n
quarkus.log.console.level=DEBUG
quarkus.log.console.color=true
quarkus.log.category."com.redhat.demos.quarkus.coffeeshop".level=DEBUG
```

TODO: Install a tool for making post requests

Post the following payload to your url (http://localhost:8080/api/favfood):
```json
{
  "customerName":"Thurston",
  "id":"67ebd8e4-1036-4c81-8f6a-61da0b27de9a",  
  "lineItems":[
    {
      "item":"BLACK_COFFEE",
      "itemId":"97370536-fc49-4580-8e9f-c02241680e47",
      "quantity":1
    },
    {
      "item":"CROISSANT",
      "itemId":"b59ccd0a-a97b-488a-a127-a5aa9bb0a646",
      "quantity":1
    },
    {
      "item":"ESPRESSO","itemId":"1779c090-2866-4bd7-af8b-917424a9656c",
      "quantity":1
    },
    {
      "item":"BLACK_COFFEE",
      "itemId":"6d2b672d-0486-4416-8862-3c8a95789879",
      "quantity":1
    },
    {
      "item":"BLACK_COFFEE",
      "itemId":"48023ea3-ec84-48e7-a794-a0a289510db1",
      "quantity":1
    },
    {
      "item":"CAKEPOP","itemId":"0b606cf6-6784-420e-b676-e8d892b02b28","quantity":1
    },
    {
      "item":"COFFEE_WITH_ROOM","itemId":"584c2219-6b51-4a2b-92ec-f45cdbdc6bdc","quantity":1
    }]}
```

Check your terminal for the output, which should be:

```shell script
17:00:56 DEBUG [co.re.de.qu.co.in.FavFoodResource] (executor-thread-199) received Order[id='67ebd8e4-1036-4c81-8f6a-61da0b27de9a', customerName='Thurston', lineItems=[LineItem[itemId='97370536-fc49-4580-8e9f-c02241680e47', item='BLACK_COFFEE', quantity=1], LineItem[itemId='b59ccd0a-a97b-488a-a127-a5aa9bb0a646', item='CROISSANT', quantity=1], LineItem[itemId='1779c090-2866-4bd7-af8b-917424a9656c', item='ESPRESSO', quantity=1], LineItem[itemId='6d2b672d-0486-4416-8862-3c8a95789879', item='BLACK_COFFEE', quantity=1], LineItem[itemId='48023ea3-ec84-48e7-a794-a0a289510db1', item='BLACK_COFFEE', quantity=1], LineItem[itemId='0b606cf6-6784-420e-b676-e8d892b02b28', item='CAKEPOP', quantity=1], LineItem[itemId='584c2219-6b51-4a2b-92ec-f45cdbdc6bdc', item='COFFEE_WITH_ROOM', quantity=1]]]
```

## Map the FavFood domain objects to the Quarkus Coffeeshop domain objects

TODO: How to install the Coffeeshop domain?

Add the following dependency to your pom.xml:
```xml
    <dependency>
      <groupId>com.redhat.quarkus</groupId>
      <artifactId>quarkus-cafe-domain</artifactId>
      <version>2.5.1</version>
    </dependency>
```

Create a class, Order.java, in domain to handle the translation:
```java
package com.redhat.demos.quarkus.coffeeshop.domain;

import com.redhat.demos.quarkus.coffeeshop.domain.favfood.FavFoodOrder;
import com.redhat.quarkus.cafe.domain.Item;
import com.redhat.quarkus.cafe.domain.LineItem;
import com.redhat.quarkus.cafe.domain.OrderInCommand;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class Order {

    Set<String> beverages;

    Set<String> foods;

    @PostConstruct
    void initializeMenu() {
        beverages = new HashSet<>();
        beverages.add("BLACK_COFFEE");
        beverages.add("COFFEE_WITH_ROOM");
        beverages.add("ESPRESSO");
        beverages.add("DOUBLE_ESPRESSO");
        beverages.add("LATTE");

        foods = new HashSet<>();
        foods.add("CAKEPOP");
        foods.add("CROISSANT");
        foods.add("MUFFIN");
        foods.add("CROISSANT_CHOCOLATE");
    }


    public OrderInCommand processFavFoodOrder(FavFoodOrder favFoodOrder) {

        OrderInCommand orderInCommand = new OrderInCommand();
        orderInCommand.id = favFoodOrder.getId();

        favFoodOrder.getLineItems().forEach(favFoodLineItem -> {
            if (beverages.contains(favFoodLineItem.getItem())) {
                orderInCommand.getBeverages().add(
                        new LineItem(
                                    translateFavFoodLineItem(favFoodLineItem.getItem()),
                                    favFoodOrder.getCustomerName()));
            }else if(foods.contains(favFoodLineItem.getItem())){
                orderInCommand.getKitchenOrders().add(
                        new LineItem(
                                translateFavFoodLineItem(favFoodLineItem.getItem()),
                                favFoodOrder.getCustomerName()));
            }
        });
        return orderInCommand;
    }

    Item translateFavFoodLineItem(String favFoodLineItem) {
        switch (favFoodLineItem) {
            case "COFFEE_BLACK":
                return Item.COFFEE_BLACK;
            case "COFFEE_WITH_ROOM":
                return Item.COFFEE_WITH_ROOM;
            case "CAPPUCCINO":
                return Item.CAPPUCCINO;
            case "ESPRESSO":
                return Item.ESPRESSO;
            case "ESPRESSO_DOUBLE":
                return Item.ESPRESSO_DOUBLE;
            case "CAKEPOP":
                return Item.CAKEPOP;
            case "CROISSANT":
                return Item.CROISSANT;
            case "CROISSANT_CHOCOLATE":
                return Item.CROISSANT_CHOCOLATE;
            case "MUFFIN":
                return Item.MUFFIN;
            default:
                return Item.ESPRESSO;
        }
    }
}
```

Update the FavFoodResource so that Order is injected and creates the OrderInCommand object that the existing application expects:

```java
package com.redhat.demos.quarkus.coffeeshop.infrastructure;

import com.redhat.demos.quarkus.coffeeshop.domain.Order;
import com.redhat.demos.quarkus.coffeeshop.domain.favfood.FavFoodOrder;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api/favfood")
public class FavFoodResource {

    Logger logger = LoggerFactory.getLogger(FavFoodResource.class);

    @Inject
    Order order;

    @POST
    public Response processFavFoodOrder(FavFoodOrder favFoodOrder) {
        logger.debug("received {}", favFoodOrder);
        OrderInCommand orderInCommand = order.processFavFoodOrder(favFoodOrder);
        logger.debug("sending {}", orderInCommand);
        return Response.accepted().build();
    }
}
```

Post the example payload again.  Your terminal should now look similar to:
```shell script
17:51:28 INFO  [io.qu.de.de.RuntimeUpdatesProcessor] (vert.x-worker-thread-0) Hot replace total time: 1.891s
17:51:28 DEBUG [co.re.de.qu.co.in.FavFoodResource] (executor-thread-198) received FavFoodOrder[id='67ebd8e4-1036-4c81-8f6a-61da0b27de9a', customerName='Thurston', lineItems=[FavFoodLineItem[itemId='97370536-fc49-4580-8e9f-c02241680e47', item='BLACK_COFFEE', quantity=1], FavFoodLineItem[itemId='b59ccd0a-a97b-488a-a127-a5aa9bb0a646', item='CROISSANT', quantity=1], FavFoodLineItem[itemId='1779c090-2866-4bd7-af8b-917424a9656c', item='ESPRESSO', quantity=1], FavFoodLineItem[itemId='6d2b672d-0486-4416-8862-3c8a95789879', item='BLACK_COFFEE', quantity=1], FavFoodLineItem[itemId='48023ea3-ec84-48e7-a794-a0a289510db1', item='BLACK_COFFEE', quantity=1], FavFoodLineItem[itemId='0b606cf6-6784-420e-b676-e8d892b02b28', item='CAKEPOP', quantity=1], FavFoodLineItem[itemId='584c2219-6b51-4a2b-92ec-f45cdbdc6bdc', item='COFFEE_WITH_ROOM', quantity=1]]]
17:51:28 DEBUG [co.re.de.qu.co.in.FavFoodResource] (executor-thread-198) sending com.redhat.quarkus.cafe.domain.OrderInCommand@3afc6825[id=67ebd8e4-1036-4c81-8f6a-61da0b27de9a,beverages=[com.redhat.quarkus.cafe.domain.LineItem@5d034156[item=ESPRESSO,name=Thurston], com.redhat.quarkus.cafe.domain.LineItem@28e130f1[item=ESPRESSO,name=Thurston], com.redhat.quarkus.cafe.domain.LineItem@2672b619[item=ESPRESSO,name=Thurston], com.redhat.quarkus.cafe.domain.LineItem@7f964fb2[item=ESPRESSO,name=Thurston], com.redhat.quarkus.cafe.domain.LineItem@3858b8e7[item=COFFEE_WITH_ROOM,name=Thurston]],kitchenOrders=[com.redhat.quarkus.cafe.domain.LineItem@199f71a5[item=CROISSANT,name=Thurston], com.redhat.quarkus.cafe.domain.LineItem@2aeecef1[item=CAKEPOP,name=Thurston]]]
```

## Send the OrderInCommand to Kafka

Modify the REST resource:

```java
package com.redhat.demos.quarkus.coffeeshop.infrastructure;

import com.redhat.demos.quarkus.coffeeshop.domain.Order;
import com.redhat.demos.quarkus.coffeeshop.domain.favfood.FavFoodOrder;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@Path("/api/favfood")
public class FavFoodResource {

    Logger logger = LoggerFactory.getLogger(FavFoodResource.class);

    private Jsonb jsonb = JsonbBuilder.create();

    @Inject
    Order order;

    @Inject
    @Channel("orders")
    Emitter<String> orderInCommandEmitter;

    @POST
    public CompletableFuture<String> processFavFoodOrder(FavFoodOrder favFoodOrder) {
        logger.debug("received {}", favFoodOrder);
        OrderInCommand orderInCommand = order.processFavFoodOrder(favFoodOrder);
        logger.debug("sending {}", orderInCommand);
        return orderInCommandEmitter.send(jsonb.toJson(orderInCommand))
                .handle((res, ex) -> {
                    if (ex != null) {
                        logger.error(ex.getMessage());
                        return ex.getMessage();
                    }else{
                        logger.debug("returning successfully");
                        return jsonb.toJson(orderInCommand);
                    }
                }).toCompletableFuture();
    }
}
```