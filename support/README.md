# Quarkus Cafe Deployment Options 

## OpenShift S2I Deployment via Ansible 

### Prerequisites
* Install Ansible 
* OpenShift Cluster

### Installation Steps using Ansible 
**Use ansible-galaxy [quarkus-cafe-demo-role](https://github.com/tosin2013/quarkus-cafe-demo-role)  role to your machine**  
```
ansible-galaxy install tosin2013.quarkus_cafe_demo_role
```

**Get the OpenShift token from your cluster**  

**Create a deployment yaml for openshift installation**  
```
cat >quarkus-cafe-deployment.yml<<YAML
- hosts: localhost
    become: yes
    vars:
      openshift_token: CHANGEME
      openshift_url: https://api.ocp4.example.com:6443 #Change this value
      insecure_skip_tls_verify: true
      project_namespace: quarkus-cafe-demo
      delete_deployment: false  
      skip_amq_install: false
      skip_quarkus_cafe_barista: false
      skip_quarkus_cafe_core: false
      skip_quarkus_cafe_kitchen: false
      skip_quarkus_cafe_web: false
      skip_quarkus_cafe_customermock: false
      quarkus_build_memory_limit: 6Gi
      quarkus_build_cpu_limit: 1
      quarkus_core_build_memory_limit: 6Gi
      domain: ocp4.example.com  # Change This value
    roles:
    - tosin2013.quarkus_cafe_demo_role
YAML

```


**Change other variables as you see fit**  

**Run ansible playbook**  
```
ansible-playbook quarkus-cafe-deployment.yml
```

## Local Deployment Instructions
```
....
```

**Example kafka consumer and producer calls**

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic orders --from-beginning
kafka-console-producer --broker-list localhost:9092 --topic orders
```

```
{"eventType":"BEVERAGE_ORDER_IN","item":"BLACK_COFFEE","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"fd2af2b9-8d97-443d-bed8-371f2782a8b3","name":"Brady","orderId":"9103dd6b-ed58-423f-90b2-5cc4314996fg"}
```

