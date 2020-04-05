{"customer": "Brady","orderId": "f6308055-993b-4827-b648-3a681141de7A","product": "COFFEE_BLACK","state": "IN_QUEUE"}
{"customer": "Brady","orderId": "f6308055-993b-4827-b648-3a681141de7A","product": "COFFEE_BLACK","state": "READY"}
{"customer": "Riley","orderId": "f6308055-993b-4827-b648-3a681141de7c","product": "CAPPUCCINO","state": "IN_QUEUE"}
{"customer": "Riley","orderId": "f6308055-993b-4827-b648-3a681141de7c","product": "CAPPUCCINO","state": "READY"}

# Build and push to dockerhub

Would not push to quay.io

Login to dockerhub with

```shell
docker login
```

quarkus.container-image.build=true
quarkus.container-image.push=true
quarkus.native.container-build=true
quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java11
quarkus.container-image.group=jeremydavis
quarkus.container-image.name=quarkus-cafe-web
quarkus.container-image.tag=latest


