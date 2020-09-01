# quarkus-cafe-barista project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Environment variables

Quarkus' configuration can be environment specific: https://quarkus.io/guides/config

This service uses the following environment variables:
* KAFKA_BOOTSTRAP_URLS

This value will need to be set locally:
```
export KAFKA_BOOTSTRAP_URLS=localhost:9092;
docker run -i --network="host" -e KAFKA_BOOTSTRAP_SERVERS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-barista:latest

```

## Local deveplomnent steps 
```
$ mv src/main/resources/application.properties src/main/resources/application.properties.openshift
$ mv src/main/resources/application.properties.development src/main/resources/application.properties
```


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `quarkus-cafe-barista-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/quarkus-cafe-barista-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/quarkus-cafe-barista-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

```
{"name": "Riley","orderId": "f6308055-993b-4827-b648-3a681141de7c","product": "CAPPUCCINO","state": "IN_QUEUE"}

{"eventType":"BEVERAGE_ORDER_IN","item":"ESPRESSO","itemId":"0c767756-572b-40c5-9287-9e124354d21f","name":"Jeremy","orderId":"ec5251a3-e434-431b-83f7-8416eea725d2"}
{"eventType":"BEVERAGE_ORDER_IN","item":"CAPPUCCINO","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b8","name":"Riley","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996f3"}
{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"
```

## Running with Docker

Quarkus' configuration can be environment specific: https://quarkus.io/guides/config

This service uses the following environment variables when running with the production profile:
* KAFKA_BOOTSTRAP_URLS

The following line will set al

```shell

export KAFKA_BOOTSTRAP_URLS=localhost:9092 

docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-barista:latest

```


### OpenShift Deployment Steps 
**Ensure that lines 9 - 15 are commented out in application.properties**
```
#quarkus.container-image.build=true
#quarkus.container-image.push=true
#quarkus.native.container-build=true
#quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java8
#quarkus.container-image.group=jeremydavis
#quarkus.container-image.name=quarkus-cafe-barista
#quarkus.container-image.tag=0.3
```
**Deploy quarkus-cafe-barista on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:20.0.0-java11~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-barista --name=quarkus-cafe-barista
```

**To delete quarkus-cafe-barista application**
```
oc delete all --selector app=quarkus-cafe-barista
```

oc run kafka-producer -ti --image=registry.access.redhat.com/amq7/amq-streams-kafka:1.1.0-kafka-2.1.1 --rm=true --restart=Never -- bin/kafka-console-producer.sh --broker-list cluster-name-kafka-bootstrap:9092 --topic my-topic