
# quarkus-cafe-web

## Environment variables

Quarkus' configuration can be environment specific: https://quarkus.io/guides/config

This service uses the following environment variables when running with the production profile:
* KAFKA_BOOTSTRAP_URLS
* STREAM_URL

## Local deveplomnent steps 
* uncomment lines 
```
#quarkus.container-image.build=true
#quarkus.container-image.push=true
#quarkus.native.container-build=true
#quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java11
#quarkus.container-image.group=jeremydavis
#quarkus.container-image.name=quarkus-cafe-web
#quarkus.container-image.tag=0.3
#quarkus.container-image.registry=quay.io
#quarkus.container-image.username=jeremydavis
#quarkus.container-image.password=

# HTTP

## Ports
%dev.quarkus.http.port=8080
```

## Example kafka message
```
{"customer": "Brady","orderId": "f6308055-993b-4827-b648-3a681141de7A","product": "COFFEE_BLACK","state": "IN_QUEUE"}
{"customer": "Brady","orderId": "f6308055-993b-4827-b648-3a681141de7A","product": "COFFEE_BLACK","state": "READY"}
{"customer": "Riley","orderId": "f6308055-993b-4827-b648-3a681141de7c","product": "CAPPUCCINO","state": "IN_QUEUE"}
{"customer": "Riley","orderId": "f6308055-993b-4827-b648-3a681141de7c","product": "CAPPUCCINO","state": "READY"}
```

## OpenShift Deployment 
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
