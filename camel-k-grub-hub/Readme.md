To run this code  on openshift:

Pre-Reqs:
- oc command line tools
- kamel command line tools 
- an OpenShift cluster

Steps:
- Login to openshift using oc commands
- Install the camel k operator via operator hub
- Create integration platform via oc tools to have access to jitpack
```
kamel install --olm=false --skip-cluster-setup --skip-operator-setup --maven-repository  https://jitpack.io@id=jitpack@snapshots
```
- Install the Integration code using kamel tools from this folder
``` 
kamel run --name=rest-with-undertow --dependency=camel-rest --dependency=camel-undertow --dependency=camel-http --dependency=camel-jackson --dependency=mvn:com.github.jeremyrdavis:quarkus-cafe-demo:1.5-SNAPSHOT --dependency=mvn:com.github.jeremyrdavis.quarkus-cafe-demo:grubhub-cafe-core:1.5-SNAPSHOT --dependency=camel-swagger-java RestWithUndertow.java
```

