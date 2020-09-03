# Event Driven Architecture with Quarkus, Kafka, and Kubernetets (Local Development)

## Table of Contents

1. Creating a Project with https://code.quarkus.io
2. Visual Studio Code
3. Getting Started with Your Project
4. Dev Mode
5. Hibernate Panache

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
* Clone the repository on your filesystem

TODO: Leave out one dependency and add it with the maven plugin later in the tutorial

## Visual Studio Code

* Open Visual Studio Code
* Open your existing workspace
* Click, "Git Clone"
* Enter the URL from your github repo

TODO: Have a workspace ready or add instructions for creating one

## Getting Started with Your Project

### Visual Studio Code

Visual Studio Code contains plugins for Java and Quarkus

TODO: Instructions for installing the plugins

### pom.xml

The selections you made are in the pom.xml
TODO: Quarkus modules

### Testing Quarkus Applications

* Open src/test/java/org/acme/ExampleResourceTest
* There are 2 ways to run tests from within VSCode:
** Click "Run Test," which can be found under the @Test annotation and above the "ExampleResourceTest" method
** Open a Terminal from within Visual Studio Code and type the following:

```shell

./mvnw clean test

```

## Dev Mode

Start Quarkus in dev mode:

```shell

./mvnw clean compile quarkus:dev

```

Open http://localhost:8080
Open http://localhost:8080/hello

### Live changes

In VS Code open the ExampleResource class (src/main/java/org/acme/ExampleResource.java)

Change the message from "hello" to something more prosaic, like "hello, world!" and save the file.  Now reload your browser

 