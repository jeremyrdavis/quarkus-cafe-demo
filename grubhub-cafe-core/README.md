This is a dependency for the model of Grubhub's Cafe orders. 

To use please utilize https://jitpack.io/

Steps:
1. Create tag after commiting
```
  git commit -m "updated jitpack"
  git tag -a 1.2-SNAPSHOT -m "my version 1.2-SNAPSHOT"
  git push origin 1.2-SNAPSHOT
```
2. Go to jitpack.io and search the repo, look under versions and click 'Get it' to kick off a build and ensure everything is building successfully
3. Reference in your kamel deployment similar to
```
kamel run --name=rest-with-undertow --dependency=camel-rest --dependency=camel-undertow --dependency=mvn:com.github.jeremyrdavis:quarkus-cafe-demo:1.2-SNAPSHOT RestWithUndertow.java
```
