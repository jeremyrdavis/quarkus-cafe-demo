# Running Locally

This is a (Supersonic, Subatomic) Java application built with Maven.  

* /pom.xml in the main directory is an aggregate pom for conveniently building all of the projects
* /quarkus-cafe-parent/pom.xml defines project dependencies and standardizes both Quarkus resources and the internal project dependencies quarkus-cafe-domain and quarkus-test-utils
* Each microservice contains its' own pom.xml that inherits from the parent

## Local Development

__NOTE__: You have to install the domain objects to run the services and the test utilities to run the tests.  See [Building](building) 

### Attaching a debugger

By default Quarkus listensn on port 5005 for a debugger.  You can change this by appending the flag, "-Ddebug<<PORT NUMBER>>" as in the below examples.  The parameter is optional, of course

### Web
export KAFKA_BOOTSTRAP_URLS=localhost:9092 STREAM_URL=http://localhost:8080/dashboard/stream CORS_ORIGINS=http://localhost:8080
./mvnw clean compile quarkus:dev

### Core
```shell
export MONGO_DB=cafedb MONGO_URL=mongodb://cafe-user:redhat-20@localhost:27017/cafedb MONGO_USERNAME=cafe-user MONGO_PASSWORD=redhat-20 KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev -Ddebug=5006
```

### Barista
```shell
export KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev -Ddebug=5007
```

### Kitchen
```shell
export KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev -Ddebug=5008
```

### Customermocker
```shell
export REST_URL=http://localhost:8080/order
./mvnw clean compile quarkus:dev  -Ddebug=5009
```

## [Building](building)

To build all of the artifacts at once run the following command in the main directory:

```shell

mvn clean install

```

You will at least need to build and install the quarkus-cafe-domain and quarkus-cafe-test-utils for the other projects to work.  You can do that in their respective directories with the standard Maven install:

```shell
mvn clean install
```

If you built the entire project they are of course already in your repo.

## Building Native Binaries and Pushing to Dockerhub

### Core

```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-core .
docker run -i --network="host" -e MONGO_DB=${MONGO_DB} -e MONGO_URL=${MONGO_URL} -e MONGO_USERNAME=${MONGO_USERNAME} -e MONGO_PASSWORD=${MONGO_PASSWORD} -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} jeremydavis/quarkus-cafe-core:2.4.0
docker images -a | grep core
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-core:<<VERSION>>
```

### Web
```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-web .
docker run -i --network="host" -e STREAM_URL=${STREAM_URL} -e CORS_ORIGINS=${CORS_ORIGINS} -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-web:latest
docker images -a | grep web
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-web:<<VERSION>>
```

### Barista
```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-barista .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} <<DOCKER_HUB_ID>>/quarkus-cafe-barista:latest
docker images -a | grep barista
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-barista:<<VERSION>>
```

### Kitchen
```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-kitchen .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} <<DOCKER_HUB_ID>>>/quarkus-cafe-kitchen:<<VERSION>>
docker images -a | grep kitchen
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-kitchen:<<VERSION>>
```

### Customermocker
```shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-customermocker .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-customermocker:<<VERSION>>
docker images -a | grep customermocker
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-customermocker:<<VERSION>>
```
  
 
