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
**Deploy quarkus-cafe-barista on OpenShift**  
```	![quarkus cafe application](support/images/webpage-example.png "quarkus appliation")
$ oc login https://api.ocp4.examaple.com:64443	
$ oc project quarkus-cafe-demo	
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java8~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-barista --name=quarkus-cafe-barista
```	


**To delete quarkus-cafe-barista application**	
```	
oc delete all --selector app=quarkus-cafe-barista	
```

**[Quarkus Cafe Deployment using S2I](support/README.md)**

## ScreenShots
![quarkus cafe topology](support/images/quarkus-cafe-applications.png "quarkus cafe topology")

![quarkus cafe kafka topics](support/images/ams-topics.png "quarkus cafe  kafka topics")


http://quarkus-cafe-web-quarkus-cafe-demo.apps.example.com/cafe example
![quarkus cafe application](support/images/webpage-example.png "quarkus appliation")


## Author Information
This was created in 2020 by [Jeremy Davis](https://github.com/jeremyrdavis)
