# Event Driven Architecture with Quarkus, Kafka, and Kubernetets

## Table of Contents

1. Creating a Project with https://code.quarkus.io
2. Code Ready Workspaces
3. Getting Started with Your Project

## Creating a Project with https://code.quarkus.io

* Open https://code.quarkus.io
* In the top left corner set the values for your microservice:
** org.j4k.tutorial.quarkus
** quarkus-cafe-tutorial
** Maven (Quarkus supports Gradle as well, but this tutorial is built with Maven )
* From the menu select 
** "RESTEasy JSON-B"
** "Hibernate ORM with Panache" 
** "JDBC Driver - PostgreSQL" 
** "JDBC Driver - H2"
** "SmallRye Reactive Messaging"
* Click "Generate Your Application" and Push to Github

## Code Ready Workspaces

* Open Code Ready Workspaces and login with your username and password
* Open your existing workspace
* Click, "Git Clone"
* Enter the URL from your github repo

TODO: Have a workspace ready or add instructions for creating one

## Getting Started with Your Project

### pom.xml

The selections you made are in the pom.xml
TODO: Quarkus modules

### Testing Quarkus Applications

* Open src/test/java/org/acme/ExampleResourceTest
 