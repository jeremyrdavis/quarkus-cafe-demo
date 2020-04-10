# ConfigMaps

# DeploymentConfig:

Add the following: 
spec:
  containers:
  ...
          envFrom:
            - configMapRef:
                name: quarkus-configmap
                
# ConfigMap

kind: ConfigMap
apiVersion: v1
metadata:
...
data:
  GREETING: 'hello, OpenShift!'
  
## Properties

### Core
${MONGO_DB}
${MONGO_URL}
${KAFKA_BOOTSTRAP_URLS}

### Barista
${KAFKA_BOOTSTRAP_URLS}

### Kitchen
${KAFKA_BOOTSTRAP_URLS}

# HTTP

# Kafka

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic web-updates --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic barista-in
```

## Create Orders

### 2 Beverages
```json
{
    "beverages": [
        {
            "item": "COFFEE_WITH_ROOM",
            "itemId": "d141c73c-ac62-4534-b5db-7c989040fecb",
            "name": "Mickey"
        },
        {
            "item": "COFFEE_BLACK",
            "itemId": "d141c73c-ac62-4534-b5db-7c989040fecc",
            "name": "Minnie"
        }
    ],
    "id": "d141c73c-ac62-4534-b5db-7c989040feca"
}
```
```json
{
    "item": "CAKEPOP",
    "itemId": "c3d154ba-13e8-4e3c-acee-c63df90f34aa",
    "name": "Goofy",
    "orderId": "1e435117-1649-42f9-9a5f-2fe71854e56c"
}
```
```json
{
    "beverages": [
        {
            "item": "COFFEE_WITH_ROOM",
            "name": "Mickey"
        },
        {
            "item": "COFFEE_BLACK",
            "name": "Minnie"
        }
    ],
    "kitchenOrders": [
        {
            "item": "CAKEPOP",
            "name": "Mickey"
        },
        {
            "item": "CROISSANT",
            "name": "Minnie"
        }
    ]
}
```

Kitchen Order Only
```json
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"4e3e194f-961a-4a02-923b-26704cf30097","name":"Laurel","orderId":"6593f77c-8d36-4570-8b27-a0bccacf0bfb"}
```



# Building and deploying

```shell
./mvnw clean package -Dquarkus.container-image.build=true -DskipTests
```