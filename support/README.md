# Quarkus Cafe Deployment Options 

## OpenShift Docker Deployment via Ansible 

### Prerequisites
* Install Ansible 
* OpenShift Cluster

### Installation Steps using Ansible 
**Use ansible-galaxy [quarkus-cafe-demo-role](https://github.com/tosin2013/quarkus-cafe-demo-role)  role to your machine**  
```shell
$ ansible-galaxy install tosin2013.quarkus_cafe_demo_role
```

**Get the OpenShift token from your cluster**  

**Create a deployment yaml for openshift installation**  
```yaml
$ cat >quarkus-cafe-deployment.yml<<YAML
- hosts: localhost
  become: yes
  gather_facts: false
  vars:
    openshift_token: << YOUR_TOKEN >>
    openshift_url:  << YOUR_URL >>
    insecure_skip_tls_verify: true
    version_barista:  << DESIRED_VERSION >>
    version_core:  << DESIRED_VERSION >>
    version_customermocker:  << DESIRED_VERSION >>
    version_kitchen:  << DESIRED_VERSION >>
    version_web:  << DESIRED_VERSION >>
    project_namespace: quarkus-cafe-demo
    delete_deployment: true
    default_owner: << YOUR_USER >>
    default_group: << YOUR_GROUP >>
    skip_amq_install: false
    amqstartingCSV: amqstreams.v1.5.3
    skip_quarkus_cafe_barista: false
    skip_quarkus_cafe_core: false
    skip_quarkus_cafe_kitchen: false
    skip_quarkus_cafe_web: false
    skip_quarkus_cafe_customermock: false
    quarkus_build_memory_limit: 6Gi
    quarkus_build_cpu_limit: 1
    quarkus_core_build_memory_limit: 6Gi
    domain: << YOUR_DOMAIN >>  # Change This value
  roles:
    - tosin2013.quarkus_cafe_demo_role
YAML
```


**Change other variables as you see fit**  

Extra variables can be added at the command line.  For example, to delete the deployment you can add the following (instead of changing the yaml file):
```
--extra-vars "delete_deployment=true"
```
**Run ansible playbook**  
```shell
$ ansible-playbook quarkus-cafe-deployment.yml
```

## Local Deployment Instructions
```
....
```

##  Deployment via Helm Chart 
[Helm Deployment for Development](helm-deployment)
[Quakrus cafe Helm Repo](https://github.com/tosin2013/quarkus-cafe-helm-chart/releases)

## Deployment via ACM - Red Hat Advanced Cluster Management for Kubernetes
[Deployment via ACM](acm-deployment)

# ConfigMaps Documentation

### DeploymentConfig:
```
Add the following: 
spec:
  containers:
  ...
          envFrom:
            - configMapRef:
                name: quarkus-configmap
```

### ConfigMap
```
kind: ConfigMap
apiVersion: v1
metadata:
...
data:
  GREETING: 'hello, OpenShift!'
```
## Properties

### Core
```
${MONGO_DB}
${MONGO_URL}
${KAFKA_BOOTSTRAP_URLS}
```

### Barista
```
${KAFKA_BOOTSTRAP_URLS}
```

### Kitchen
```
${KAFKA_BOOTSTRAP_URLS}
```

### Customermock
```
${REST_URL}
```
# HTTP

# Kafka

```shell
kafka-console-consumer --bootstrap-server localhost:9092 --topic web-updates --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic barista-in
kafka-console-producer --broker-list localhost:9092 --topic web-in
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

{"beverages": [{"item": "COFFEE_WITH_ROOM","itemId": "d141c73c-ac62-4534-b5db-7c989040fecb","name": "Mickey"},{"item": "COFFEE_BLACK","itemId": "d141c73c-ac62-4534-b5db-7c989040fecc","name": "Minnie"}],"id": "d141c73c-ac62-4534-b5db-7c989040feca"}
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

{"beverages": [{"item": "COFFEE_WITH_ROOM","name": "Mickey"},{"item": "COFFEE_BLACK","name": "Minnie"}],"kitchenOrders": [{"item": "CAKEPOP","name": "Mickey"},{"item": "CROISSANT","name": "Minnie"}]}

Kitchen Order Only
```json
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"4e3e194f-961a-4a02-923b-26704cf30097","name":"Laurel","orderId":"6593f77c-8d36-4570-8b27-a0bccacf0bfb"}
```



# Building and deploying

```shell
./mvnw clean package -Dquarkus.container-image.build=true -DskipTests
```