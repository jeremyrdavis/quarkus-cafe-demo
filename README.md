# OpenShift Deployment 

### Prerequisites
* Create quarkus-cafe-demo project 
* Deploy AMQ Operator 

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


**Example kafka consumer and producer call**
```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic orders
```

```
{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
```