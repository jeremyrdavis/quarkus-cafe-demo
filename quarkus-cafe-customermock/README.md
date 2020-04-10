# quarkus-cafe-customermock project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .


## OpenShift Deployment 
**Deploy quarkus-cafe-customermock on OpenShift**
```
$ oc login https://api.ocp4.examaple.com:64443
$ oc project quarkus-cafe-demo
$ oc new-app quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java8-openshift~https://github.com/jeremyrdavis/quarkus-cafe-demo.git --context-dir=quarkus-cafe-customermock --name=quarkus-cafe-customermock
```

**To delete quarkus-cafe-barista application**
```
$ oc delete all --selector app=quarkus-cafe-customermock
```

## Local deveplomnent steps 
* uncomment lines 
```
#quarkus.container-image.build=true
#quarkus.container-image.push=true
#quarkus.native.container-build=true
#quarkus.jib.base-native-image=quay.io/quarkus/ubi-quarkus-native-image:20.0.0-java11
#quarkus.container-image.group=jeremydavis
#quarkus.container-image.name=quarkus-cafe-customermocker
#quarkus.container-image.tag=0.3

# HTTP

%dev.quarkus.http.port=8084
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `quarkus-cafe-customermock-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/quarkus-cafe-customermock-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/quarkus-cafe-customermock-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .