# Quarkus Cafe on ACM
![](../images/acm-quarkus-cafe-app.png)

## Provision RHPDS Enviornment 
* OCP4 ACM Hub
* OCP4 ACM Managed


## Install ACM Managed and Configure a HUB

Import an existing cluster
![](https://i.imgur.com/IFdi3Ez.png)

Run Genereated command on target machine 
![](https://i.imgur.com/6inP821.png)

View Cluster status
![](https://i.imgur.com/YwLk7w4.png)

## Administrator Tasks On Target Cluster (OCP4 ACM Managed)


### Clone quarkus cafe demo repo locally 
```
git clone https://github.com/jeremyrdavis/quarkus-cafe-demo.git
```

### OpenShift 4.x Instructions 
**Login to OpenShift and create project**
```
oc new-project quarkus-cafe-demo
```

**cd into admin tasks directory**
```
cd quarkus-cafe-demo/support/helm-deployment/admin-tasks
```

**Run ansible playbook to install Red Hat AMQ and mongodb on target clusters**
* [admin-tasks](https://github.com/jeremyrdavis/quarkus-cafe-demo/blob/master/support/helm-deployment/admin-tasks/README.md)

**cd back into acm directory**  
```
cd ../../acm-deployment/
```

## Configure OpenShift client context for cluster admin access 
```
# Login into hub cluster 
oc login -u admin -p XXXX --insecure-skip-tls-verify https://api.YOURCLUSTER1.DOMAIN:6443
# Set the name of the context
oc config rename-context $(oc config current-context) hubcluster
# Login into 1st cluster (A environment)
oc login -u admin -p XXXX --insecure-skip-tls-verify https://api.YOURCLUSTER1.DOMAIN:6443
# Set the name of the context
oc config rename-context $(oc config current-context) cluster1
# Login into 2nd cluster (B environment) ::: OPTIONAL
oc login -u admin -p XXXX --insecure-skip-tls-verify https://api.YOURCLUSTER2.DOMAIN:6443
# Set the name of the context
oc config rename-context $(oc config current-context) cluster2
# Login into 3rd cluster (C environment) ::: OPTIONAL
oc login -u admin -p XXXX --insecure-skip-tls-verify https://api.YOURCLUSTER3.DOMAIN:6443
# Set the name of the context
oc config rename-context $(oc config current-context) cluster3
```

# Test the different cluster contexts
```
# Switch to hub
oc config use-context hubcluster
# Switch to cluster1
oc config use-context cluster1
# List the nodes in cluster1
oc get nodes
# Switch to cluster2
oc config use-context cluster2
# List the nodes in cluster2
oc get nodes
# Switch to cluster3 ::: Optional 
oc config use-context cluster3
# List the nodes in cluster3 ::: Optional 
oc get nodes
# Switch back to cluster1
oc config use-context cluster1
```

## Deploy Quarkus cafe Application on ACM Hub
**From ACM**
Login to ACM Managed clusater (OCP4 ACM Hub)

**Update routes for Quarkus Cafe Application**
```
cp overlays/cluster1/route.yaml.backup overlays/cluster1/route.yaml
cp overlays/cluster2/route.yaml.backup overlays/cluster2/route.yaml
cp overlays/cluster3/route.yaml.backup overlays/cluster3/route.yaml

# Define the variable of `ROUTE_CLUSTER1`
ROUTE_CLUSTER1=quarkus-cafe-web-quarkus-cafe-demo.$(oc --context=cluster1 get ingresses.config.openshift.io cluster -o jsonpath='{ .spec.domain }')

# Define the variable of `ROUTE_CLUSTER2`
ROUTE_CLUSTER2=quarkus-cafe-web-quarkus-cafe-demo.$(oc --context=cluster2 get ingresses.config.openshift.io cluster -o jsonpath='{ .spec.domain }')

# Define the variable of `ROUTE_CLUSTER3`  ::: OPTIONAL
ROUTE_CLUSTER3=quarkus-cafe-web-quarkus-cafe-demo.$(oc --context=cluster3 get ingresses.config.openshift.io cluster -o jsonpath='{ .spec.domain }')

# Replace the value of changeme with `ROUTE_CLUSTER1` in the file `route.yaml`
sed -i "s/changeme/${ROUTE_CLUSTER1}/" overlays/cluster1/route.yaml

# Replace the value of changeme with `ROUTE_CLUSTER2` in the file `route.yaml`
sed -i "s/changeme/${ROUTE_CLUSTER2}/" overlays/cluster2/route.yaml

# Replace the value of changeme with `ROUTE_CLUSTER3` in the file `route.yaml`  ::: OPTIONAL
sed -i "s/changeme/${ROUTE_CLUSTER3}/" overlays/cluster3/route.yaml
```

**Use context hubcluster**
```
oc config use-context hubcluster
```

**Create namespace for subscription**
```
oc create -f acm-configs/01_namespace.yaml
```

**Create a Channel**
```
oc create -f acm-configs/02_channel.yaml
```

**Create application**
```
oc create -f 03_application_webapp.yaml
```

**Confirm Clusters are properly labeled**
*  `clusterid=cluster1`
*  `clusterid=cluster2`
*  `clusterid=cluster3`
*  `clusterid=...`

**Create placement rules**
```
oc create -f 04_placement_cluster1.yaml
oc create -f 04_placement_cluster2.yaml
oc create -f 04_placement_cluster3.yaml
```

**Create subscription**
```
oc create -f 05_subscription_cluster1.yaml
oc create -f 05_subscription_cluster2.yaml
oc create -f 05_subscription_cluster3.yaml
```

**Verify the deployments have been created on all the clusters.**