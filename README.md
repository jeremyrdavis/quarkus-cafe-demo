# Quarkus, Kafka, Kubernetes, and Coffee

This repo contains an event-driven demo application built with Quarkus, AMQ Streams (Kafka), and MongoDB.  The application can run locally and in OpenShift (Kubernetes.)

## Overview

The application consists of 4 microservices:
* Web
* Core
* Kitchen
* Barista

There is an additional microservice used for testing: Customermock

## Quarkus Cafe Deployment Instructions 
* [Quarkus Cafe Deployment Options](support/README.md)

## ScreenShots
![quarkus cafe topology](support/images/quarkus-cafe-applications.png "quarkus cafe topology")

![quarkus cafe kafka topics](support/images/ams-topics.png "quarkus cafe  kafka topics")


http://quarkus-cafe-web-quarkus-cafe-demo.apps.example.com/cafe example
![quarkus cafe application](support/images/webpage-example.png "quarkus appliation")


## Author Information
This was created in 2020 by [Jeremy Davis](https://github.com/jeremyrdavis)