# Event Driven Architecture with Quarkus, Kafka, and Kubernetets (Local Development)

In this workshop you will build a microservice to integrate the existing Quarkus Coffeeshop application with the FavFood Delivery Service

## Table of Contents

1. Creating a Project with https://code.quarkus.io
2. Visual Studio Code
3. Getting Started with Your Project
4. Dev Mode
5. Hibernate Panache

## Pre-requisites

You need:
* a JDK installed on your machine
* a Github account
* an IDE (the examples will use Visual Studio Code)
-- Visual Studio Code
-- IntelliJ

## Application Requirements

FavFood has supplied us with an [OpenApi](https://www.openapis.org/) document describing the service that we need to stand up in order to integrate with them.  Don't worry if you are unfamiliar with the OpenAPI spec, but it is worth checking out after the workshop: [SwaggerIO OpenAPI Specification](https://swagger.io/specification/)

```yaml
---
openapi: 3.0.1
info:
  title: FavFood Delivery Service API
  version: "1.0"
paths:
  /api:
    post:
      responses:
        "202":
          description: OK
          content:
            'application/json':
              schema:
                $ref: '#/components/schemas/Order'
components:
  schemas:
    Order:
      type: object
      properties:
        customerName:
          type: string
        id:
          type: string
        lineItems:
          $ref: '#/components/schemas/ListLineItem'
    ListLineItem:
      type: array
      items:
        $ref: '#/components/schemas/LineItem'
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
```

We need to accept an "Order" object with properties, "customerName," "id," and an array "listLineItems" of "LineIem" objects defined.  The "LineItem" contains Strings for "itemId," "item," and an integer "quantity."

## Creating a Project with https://code.quarkus.io

* Open https://code.quarkus.io
* In the top left corner set the values for your microservice:
** org.j4k.workshops.quarkus
** quarkus-coffeeshop-workshop
** Maven (Quarkus supports Gradle as well, but this tutorial is built with Maven )
* From the menu select 
** "RESTEasy JSON-B"
** "Hibernate ORM with Panache" 
** "JDBC Driver - PostgreSQL" 
** "JDBC Driver - H2"
** "SmallRye Reactive Messaging"
* Click "Generate Your Application" and Push to Github
* Clone the repository on your filesystem

TODO: Leave out one dependency and add it with the maven plugin later in the tutorial

## Visual Studio Code

* Open Visual Studio Code
* Open your existing workspace
* Click, "Git Clone"
* Enter the URL from your github repo

TODO: Have a workspace ready or add instructions for creating one

## Getting Started with Your Project

### Visual Studio Code

Visual Studio Code contains plugins for Java and Quarkus

TODO: Instructions for installing the plugins

### pom.xml

The selections you made are in the pom.xml :

```xml
  <dependencies>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-junit5</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.rest-assured</groupId>
      <artifactId>rest-assured</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-jdbc-h2</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy-jsonb</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-reactive-messaging</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-hibernate-orm-panache</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-jdbc-postgresql</artifactId>
    </dependency>
  </dependencies>
```

For more on [Quarkus modules](https://quarkus.io/guides/writing-extensions)


### Testing Quarkus Applications

* Open src/test/java/org/j4k/workshops/quarkus/ExampleResourceTest
* There are 2 ways to run tests from within VSCode:
** Click "Run Test," which can be found under the @Test annotation and above the "ExampleResourceTest" method
** Open a Terminal from within Visual Studio Code and type the following:

```shell

./mvnw clean test

```
### Quarkus Tests

You have probably noticed that the test classes are annotated with "@QuarkusTest."  The test spins up a version of Quarkus, calls the endpoint using rest-assured, and verifies the output using [Rest-Assured](https://rest-assured.io)

## Dev Mode

quarkus:dev runs Quarkus in development mode. This enables hot deployment with background compilation, which means that when you modify your Java files and/or your resource files and refresh your browser, these changes will automatically take effect. This works too for resource files like the configuration property file. Refreshing the browser triggers a scan of the workspace, and if any changes are detected, the Java files are recompiled and the application is redeployed; your request is then serviced by the redeployed application. If there are any issues with compilation or deployment an error page will let you know.
This will also listen for a debugger on port 5005. If you want to wait for the debugger to attach before running you can pass -Dsuspend on the command line. If you don’t want the debugger at all you can use -Ddebug=false.
- [QUARKUS - CREATING YOUR FIRST APPLICATION](https://quarkus.io/guides/getting-started#development-mode)

Start Quarkus in dev mode:

```shell

./mvnw clean compile quarkus:dev

```
Open http://localhost:8080
Open http://localhost:8080/hello

### Live changes

In VS Code open the ExampleResource class (src/main/java/org/j4k/workshops/quarkus/ExampleResource.java)

Change the message from "hello" to something more prosaic, like "hello, world!" and save the file.  Now reload your browser.

The change is almost instantaneous.  

Change the message again to something even better, like, "hello, j4k!" and reload your browser.  Play around until you have a hello message you like

Quarkus Code Wisdom: *Coding is mostly trial and error.  The faster your feedback loop the more productive you will be!*

Once you're happy with the message, and your test are passing you can commit your code.  Of course the tests aren't going to pass no that we've changed the message

### Some More Testing

You can run the test again without stopping Quarkus.  The tests use a different port so you can keep Quarkus runing in dev mode and run tests at the same time so we can fail our test while the app is running

#### Parameterize the Greeting Message

Let's parameterize the message by moving it into the application.properties file found in src/main/resources by adding the following:

```properties
# Configuration file
# key = value

%dev.hello.message=Hello, J4K 2020!
%test.hello.message=hello
hello.message=Hello from Quarkus!
```

As an astute developer you will have noticed that the properties are parameterized ("%dev.hello.message".)  You can add "%prod" if you like, but Quarkus will grab the non-parameterized property for prod by default.  

#### Update ExampleResource

Now we need to update ExampleResource.java to use the parameter:

```java
package org.j4k.workshops.quarkus;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @ConfigProperty(name="hello.message")
    String helloMessage;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return helloMessage;
    }
}
```

Refresh your browser.  You should of course see the new %dev.hello.message.

Re-run the test, and this time you should pass.  You can also parameterize the ExampleResourceTest.java:

```java
package org.j4k.workshops.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.resource.spi.ConfigProperty;

@QuarkusTest
public class ExampleResourceTest {

    @ConfigProperty(name="hello.message")
    String helloMessage;

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is(helloMessage));
    }

}
```


You can commit the changes to github if you want:

```shell script
git commmit -am "Parameterized ExampleResource message"
```

## Our Application

### Let's Write a Test and Fail Fast

Let's create a new package, "org.j4k.workshops.quarkus.infrastructure," and a test, "FavFoodResourceTest" for our REST service:

```java
package org.j4k.workshops.quarkus.infrastucture;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class FavFoodResourceTest {

    @Test
    public void testFavFoodEndpoint() {

        final JsonObject order = mockOrder();

        given()
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .body(order.toString())
          .when()
          .post("/FavFood")
          .then()
             .statusCode(202)
             .body(is(order.toString()));
    }

    JsonObject mockOrder(){
        return Json.createObjectBuilder()
        .add("customerName", "Lemmy")
        .add("id", UUID.randomUUID().toString())
        .add("lineItems", mockLineItems()).build();    
    }

	private JsonArray mockLineItems() {
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        arrayBuilder.add(mockLineItem());
		return arrayBuilder.build();
	}

	private JsonObject mockLineItem() {
        return Json.createObjectBuilder()
        .add("item", "BLACK_COFFEE")
        .add("id", UUID.randomUUID().toString())
        .add("quantity", 1).build();    
	}

}
```
#### Rest Assured Test

The Rest-Assured part of our test is:

```java
    @Test
    public void testFavFoodEndpoint() {

        final JsonObject order = mockOrder();

        given()
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .body(order.toString())
          .when()
          .post("/FavFood")
          .then()
             .statusCode(202)
             .body(is(order.toString()));
    }
```

Most of it is similar to our earlier test.  The differences are that we have added header information and a body to the POST request.

#### Mock Out an Example Payload

```java
    JsonObject mockOrder(){
        return Json.createObjectBuilder()
        .add("customerName", "Lemmy")
        .add("id", UUID.randomUUID().toString())
        .add("lineItems", mockLineItems()).build();    
    }

	private JsonArray mockLineItems() {
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        arrayBuilder.add(mockLineItem());
		return arrayBuilder.build();
	}

	private JsonObject mockLineItem() {
        return Json.createObjectBuilder()
        .add("item", "BLACK_COFFEE")
        .add("id", UUID.randomUUID().toString())
        .add("quantity", 1).build();    
	}
```

These three methods create the JSON payload we will send to our REST endpoint.  We are using [JSON-B](http://json-b.net/) which is one of the Json libraries available in Quarkus.  Jackson is also available if you prefer their API's.

Run the test.  It should of course fail because we haven't implemented our endpoint yet.

### Implement Our Endpoint

Create the "infrastructure" package in the "src/java" folder.  Create a Java class, "org.j4k.workshops.quarkus.infrastructure.FavFoodResource" with the following content:

```java
package org.j4k.workshops.quarkus.infrastructure;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("FavFood")
public class FavFoodResource {

    @POST
    public Response placeOrder(final FavFoodOrder favFoodOrder){
        return Response.accepted().entity(favFoodOrder).build();
    }
}
```

Our class won't compile of course because FavFoodOrder doesn't exist.