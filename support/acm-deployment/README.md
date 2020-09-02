# Quarkus Cafe on ACM

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

**Run ansible playbook to install Red Hat AMQ and mongodb on target cluster**
* [admin-tasks](https://github.com/jeremyrdavis/quarkus-cafe-demo/blob/master/support/helm-deployment/admin-tasks/README.md)


## Deploy Quarkus cafe Application on ACM Hub
**From ACM**
Login to ACM Managed clusater (OCP4 ACM Hub)

Create namespace for subscription
**Create namespace for subscription**
```
cat <<EOF | oc create -f -
---
apiVersion: v1
kind: Namespace
metadata:
  name: quarkus-cafe-demo
EOF
```

**Create application definition**
```
cat <<EOF | oc create -f -
---
apiVersion: app.k8s.io/v1beta1
kind: Application
metadata:
  labels:
    app: quarkus-cafe-details
  name: quarkus-cafe-app
  namespace: quarkus-cafe-demo
spec:
  componentKinds:
  - group: apps.open-cluster-management.io
    kind: Subscription
  selector:
    matchLabels:
      app: quarkus-cafe-details
status: {}
EOF
```
![](https://i.imgur.com/LDOBpeh.png)

**Create namespace channel definition**
```
cat <<EOF | oc create -f -
---
apiVersion: v1
kind: Namespace
metadata:
  name: quarkus-cafe-ch-ns
EOF
```

**Create Channel definition in this example we are using helm**
```
cat <<EOF | oc create -f -
---
apiVersion: apps.open-cluster-management.io/v1
kind: Channel
metadata:
  name: quarkus-cafe-ch
  labels:
    app: quarkus-cafe-details
  namespace: quarkus-cafe-ch-ns
spec:
  type: HelmRepo
  pathname: https://tosin2013.github.io/quarkus-cafe-helm-chart/
EOF
```


**Create Subscription definition for application**
```
cat <<EOF | oc create -f -
---
apiVersion: apps.open-cluster-management.io/v1
kind: Subscription
metadata:
  name: quarkus-cafe
  namespace: quarkus-cafe-demo
  labels:
    app: quarkus-cafe-details
spec:
  channel: quarkus-cafe-ch-ns/quarkus-cafe-ch
  name: quarkus-helm-chart
  packageFilter:
    version: "0.1.3"
  placement:
    placementRef:
      kind: PlacementRule
      name: towhichcluster
  overrides:
  - clusterName: "/"
    clusterOverrides:
    - path: "quarkus-cafe-demo"
      value: default
EOF
```

**Set Placement Rule for application**
```
cat <<EOF | oc create -f -
---
apiVersion: apps.open-cluster-management.io/v1
kind: PlacementRule
metadata:
  name: towhichcluster
  namespace: quarkus-cafe-demo
  labels:
    app: quarkus-cafe-details
spec:
  clusterSelector:
    matchLabels:
      purpose: dev
EOF
```

