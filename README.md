# Quarkus, Kafka, Kubernetes, and Coffee

This repo contains an event-driven demo application built with Quarkus, AMQ Streams (Kafka), and MongoDB.  The application can run locally and in OpenShift (Kubernetes.)

## Overview

The application consists of 4 microservices:
* Web
* Core
* Kitchen
* Barista

There is an additional microservice used for testing: Customermock

### Web

quarkus-cafe-web

This service hosts the web front end and is the initial entry point for all orders.  Orders are sent to a Kafka topic, web-in, where they are picked up by the Core service.

This services listens to the web-updates topic and pushes updates to the web front end.

### Core

quarkus-cafe-core

This service handles the business logic of creating

### Support

The support folder contains
Supporting scripts can be found in the "support" folder

## OpenShift Deployment 

### Prerequisites
* Create quarkus-cafe project 
* Deploy AMQ Streams Operator 

### Installation Steps

**Deploy quarkus-cafe-barista on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java8-openshift~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-barista --name=quarkus-cafe-barista
```

**To delete quarkus-cafe-barista application**
```
oc delete all --selector app=quarkus-cafe-barista
```

## Local Environment

**Example kafka consumer and producer calls**

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic orders
```

```
{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
```

## Quarkus Cafe Deployment Options 
* [Quarkus Cafe Deployment Options ](support/README.md)

## ScreenShots
![quarkus cafe topology](support/images/quarkus-cafe-applications.png "quarkus cafe topology")

![quarkus cafe kafka topics](support/images/ams-topics.png "quarkus cafe  kafka topics")


http://quarkus-cafe-web-quarkus-cafe-demo.apps.example.com/cafe example
![quarkus cafe application](support/images/webpage-example.png "quarkus application")


Landing Page
![quarkus cafe application landing page](support/images/landing-page.png "quarkus application landing page")

## Author Information
This was created in 2020 by [Jeremy Davis](https://github.com/jeremyrdavis)

## Local Development

### Supporting infrastructure (Kafka and MongoDB)

There is a Docker Compose file in the "support" directory.  Run it with:

```shell

docker-compose up

```

This will start both Kafka and MongoDB and run the "init-mongo.js" init script to setup the appropriate user for MongoDB.

### Kafka

To monitor the Kafka topics open terminal windows and run the following commands:

```shell

kafka-console-consumer --bootstrap-server localhost:9092 --topic web-updates --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic barista-in

```

### MongoDB

The following will open a bash terminal on the mongo container:

```shell

docker exec -it quarkus-mongodb-container bash

```

To verify inserts, authenticate and query the db, "cafedb":

```shell

use cafedb
db.auth("cafeuser","redhat-20")
show collections
db.Order.find()

```


### Quarkus Microservices

All microservices can be started in Quarkus' Dev Mode from their respective directories with:

```shell

./mvnw clean compile quarkus:dev

```
