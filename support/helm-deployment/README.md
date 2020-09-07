# Deploy Quarkus Cafe Using a helm chart

## Administrator Tasks 
    
**Install Helm**
```
$ curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3
$ chmod 700 get_helm.sh
$ ./get_helm.sh
```

### OpenShift 4.x Instructions 
**Login to OpenShift and create project**
```
oc new-project quarkus-cafe-demo
```

**Run ansible playbook to install Red Hat AMQ and mongodb on target cluster**
* [admin-tasks](admin-tasks/README.md)


### Kubernertes Cluster Instructions - WIP
**Add the Strimzi Helm Chart repository**
```
helm repo add strimzi https://strimzi.io/charts/
```

**Deploy the Cluster Operator using the Helm command line tool**
```
helm install strimzi/strimzi-kafka-operator
```

**Deploy monogodb using helm chart**  
work in progress

## Developer Tasks 
**cd into support/helm-deployment**

**Update the default values found in quarkus-helm-chart/values.yaml** 

**Test Install quarkus application**
```
helm install quarkus-cafe-deployment quarkus-helm-chart/ --values quarkus-helm-chart/values.yaml --dry-run
```

**Install quarkus application**
```
helm install quarkus-cafe-deployment quarkus-helm-chart/ --values quarkus-helm-chart/values.yaml 
```
**Uninstall quarkus application**
```
helm uninstall quarkus-cafe-deployment
```