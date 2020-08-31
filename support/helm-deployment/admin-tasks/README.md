Admin Tasks 
=========

Install AMQ Steams and MongoDB on OpenShift before running helm Chart.

Requirements
============
* Install Ansible  
* Install OpenShift CLi
* cluster-admin access  to OpenShift cluster

**Optional: Install OpenShift CLI**
```
curl -OL https://mirror.openshift.com/pub/openshift-v4/clients/ocp/latest/openshift-client-linux.tar.gz
tar -zxvf openshift-client-linux.tar.gz
mv oc /usr/local/bin
mv kubectl /usr/local/bin
chmod +x /usr/local/bin/o
chmod +x /usr/local/bin/kubectl
oc version
kubectl version
```

Run the following commands 
========================
**cd into support/helm-deployment/admin-tasks**

**Create variables file for inventory**
```
cat >quarkus_vars.json<<EOF
{
   "openshift_token": 123456789,
   "openshift_url": "https://api.ocp4.example.com:6443",
   "insecure_skip_tls_verify": true,
   "default_owner": "root",
   "default_group": "root",
   "project_namespace": "quarkus-cafe-demo",
   "delete_deployment": false,
   "remote_username": "root"
}
EOF
```

**Run ansible playbook**
```
ansible-playbook tests/test.yml --extra-vars "@quarkus_vars.json"
```

**Verifiy Deployment**
```
# oc get pods
NAME                                            READY   STATUS      RESTARTS   AGE
cafe-cluster-entity-operator-7f5bc48f4c-bw5s6   3/3     Running     0          78s
cafe-cluster-kafka-0                            2/2     Running     0          117s
cafe-cluster-kafka-1                            2/2     Running     0          117s
cafe-cluster-kafka-2                            2/2     Running     0          117s
cafe-cluster-zookeeper-0                        1/1     Running     0          3m14s
cafe-cluster-zookeeper-1                        1/1     Running     0          3m14s
cafe-cluster-zookeeper-2                        1/1     Running     0          3m14s
mongodb-1-deploy                                0/1     Completed   0          50s
mongodb-1-z448g                                 1/1     Running     0          47s
```

Troubleshooting
===============
* To update the amq version edit the defaults/main.yml 
```
amqstartingCSV: amqstreams.v1.5.3
```

