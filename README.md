# OpenShift Deployment 

### Prerequisites
* Create quarkus-cafe-demo project 
* Deploy ARed Hat Integration - AMQ Streams 
  * Kafka Topic
  * Configure topics
* Deploy Mongo DB

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

**To delete quarkus-cafe-core application**
```
$ oc delete all --selector app=quarkus-cafe-core
```

**Deploy quarkus-cafe-kitchen on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-kitchen --name=quarkus-cafe-kitchen
```

**To delete quarkus-cafe-kitchen application**
```
$ oc delete all --selector app=quarkus-cafe-kitchen
```

**Deploy quarkus-cafe-web on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-web --name=quarkus-cafe-web
```

**To delete quarkus-cafe-web application**
```
$ oc delete all --selector app=quarkus-cafe-web
```

**Deploy quarkus-cafe-customermock on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-customermock --name=quarkus-cafe-customermock
$ oc expose svc/quarkus-cafe-web
```

**To delete quarkus-cafe-barista application**
```
$ oc delete all --selector app=quarkus-cafe-customermock
```

**Example kafka consumer and producer call**
```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic orders
```

```
{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
```