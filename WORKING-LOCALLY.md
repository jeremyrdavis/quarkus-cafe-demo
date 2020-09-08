# Running Locally

## Core
'''shell
export MONGO_DB=cafedb MONGO_URL=mongodb://cafe-user:redhat-20@localhost:27017/cafedb MONGO_USERNAME=cafe-user MONGO_PASSWORD=redhat-20 KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev
'''

### Building a Native Binary and Pushing to Dockerhub
'''shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-core .
docker run -i --network="host" -e MONGO_DB=${MONGO_DB} -e MONGO_URL=${MONGO_URL} -e MONGO_USERNAME=${MONGO_USERNAME} -e MONGO_PASSWORD=${MONGO_PASSWORD} -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} jeremydavis/quarkus-cafe-core:2.4.0
docker images -a | grep core
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-core:<<VERSION>>
'''

## Web
export KAFKA_BOOTSTRAP_URLS=localhost:9092 STREAM_URL=http://localhost:8080/dashboard/stream CORS_ORIGINS=http://localhost:8080
./mvnw clean compile quarkus:dev

### Building a Native Binary and Pushing to Dockerhub
'''shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-web .
docker run -i --network="host" -e STREAM_URL=${STREAM_URL} -e CORS_ORIGINS=${CORS_ORIGINS} -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-web:latest
docker images -a | grep web
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-web:<<VERSION>>
'''

## Barista
'''shell
export KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev
'''

### Building a Native Binary and Pushing to Dockerhub
'''shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-barista .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-barista:<<VERSION>>
docker images -a | grep barista
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-barista:<<VERSION>>
'''

## Kitchen
'''shell
export KAFKA_BOOTSTRAP_URLS=localhost:9092
./mvnw clean compile quarkus:dev
'''

### Building a Native Binary and Pushing to Dockerhub
'''shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-kitchen .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-kitchen:<<VERSION>>
docker images -a | grep kitchen
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-kitchen:<<VERSION>>
'''

## Customermocker
'''shell
export REST_URL=http://localhost:8080/order
./mvnw clean compile quarkus:dev
'''

### Building a Native Binary and Pushing to Dockerhub
'''shell
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t <<DOCKER_HUB_ID>>/quarkus-cafe-customermocker .
docker run -i --network="host" -e KAFKA_BOOTSTRAP_URLS=${KAFKA_BOOTSTRAP_URLS} quarkus-cafe-demo/quarkus-cafe-customermocker:<<VERSION>>
docker images -a | grep customermocker
docker tag <<RESULT>> <<DOCKER_HUB_ID>>/quarkus-cafe-customermocker:<<VERSION>>
'''
