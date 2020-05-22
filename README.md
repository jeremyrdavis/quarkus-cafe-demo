# Quarkus, Kafka, Kubernetes, and Coffee

This repo contains an event-driven demo application built with Quarkus, AMQ Streams (Kafka), and MongoDB.  The application can run locally and in OpenShift (Kubernetes.)

## Overview

The application consists of 4 microservices:
* Web
* Core
* Kitchen
* Barista

There is an additional microservice used for testing: Customermock


## OpenShift Deployment 

### Prerequisites
* Create quarkus-cafe project 
* Deploy AMQ Streams Operator 

### Installation Steps
**Deploy AMQ**
```
$cat >amq-subscription.yaml<<YAML
apiVersion: operators.coreos.com/v1alpha1
kind: Subscription
metadata:
  generation: 1
  name: amq-streams
  namespace: openshift-operators
spec:
  channel: stable
  installPlanApproval: Automatic
  name: amq-streams
  source: redhat-operators
  sourceNamespace: openshift-marketplace
  startingCSV: amqstreams.v1.4.0
YAML
```

**Deploy mongodb**
```
$ oc new-app --name=mongodb --template=mongodb-ephemeral \
  -e MONGODB_USER=mongodb \
  -e MONGODB_PASSWORD=mongodb \
  -e MONGODB_DATABASE=cafedb \
  -e MONGODB_ADMIN_PASSWORD=mongodb
```

**Deploy quarkus-cafe-barista on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-barista --name=quarkus-cafe-barista 
```

**To delete quarkus-cafe-barista application**
```
$ oc delete all --selector app=quarkus-cafe-barista
```

**Deploy quarkus-cafe-core on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java8~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-core --name=quarkus-cafe-core 
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

