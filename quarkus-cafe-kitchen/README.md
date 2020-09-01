# Kitchen Microservice

## Local deveplomnent steps 
* uncomment lines 
```
#quarkus.container-image.build=true
#quarkus.container-image.push=true
#quarkus.native.container-build=true
#quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java11
#quarkus.container-image.group=jeremydavis
#quarkus.container-image.name=quarkus-cafe-kitchen
#quarkus.container-image.tag=0.3

#change
# Ports
%dev.quarkus.http.port=8083
```
## Environment variables

Quarkus' configuration can be environment specific: https://quarkus.io/guides/config

This service uses the following environment variables:
* KAFKA_BOOTSTRAP_URLS

This value will need to be set locally:
```
export KAFKA_BOOTSTRAP_URLS=localhost:9092;
docker run -i --network="host" -e KAFKA_BOOTSTRAP_SERVERS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-kitchen:latest

```

## OpenShift Deployment 
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


# Tests

Run the integration tests with:

```shell
./mvnw clean verify
```
# Sample messages for Kafka
```shell
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Marcus","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"COOKIE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Anthony","orderId":"6353dd6b-ed58-423f-90b2-5cc431499pbbv"}
{"eventType":"KITCHEN_ORDER_IN","item":"CAKEPOP","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Bruno","orderId":"9103dd6b-ed58-423f-90b2-5cc43147E5GH"}
{"eventType":"KITCHEN_ORDER_IN","item":"COOKIE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Mason","orderId":"9103dd6b-ed58-423f-90b2-5cc431134cc%"}
```