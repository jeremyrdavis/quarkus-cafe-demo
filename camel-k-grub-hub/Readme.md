To run this code  on openshift:

Pre-Reqs:
- oc command line tools
- kamel command line tools 
- an OpenShift cluster

Steps:
- Login to openshift using oc commands
- Install the camel k operator via operator hub
- Add an Integration Platform from the operator view, defaults are fine
- In your command line run `kamel run --name=rest-with-undertow --dependency=camel-rest --dependency=camel-undertow RestWithUndertow.java`

