# Gergo's recipe assignment

## Starting the application

### Prequisites (what I use)

* Apache Maven 3.8.4
* Openjdk 18
* Created and tested on Manjaro Linux

### Creating runnable JAR and starting the application

Run the following command in project root folder, wnere pom.xml file is:

```mvn clean package```

This creates a runnable JAR file in target folder. To start the application run:

```java -jar recipeApp.jar```

## Usage

[SWAGGER](docs/openapi.json)

While the application is running: [Interactive docs](http://localhost:8080/swagger-ui/index.html)

```http://localhost:8080/recipe```

## Architectural choices

### SpringBoot

Spring boot is for me the easiest and fastest way to create REST API applications. It is easy to deploy, has embedded webserver, and requires minimal configuration.

### Database

For persistence, I chose HSQLDB. It is integrated in the running application, so there is no need to deploy a separate database anywhere else, which is ideal for a project like this.

### SpringDocs

SpringDocs provides a nice and easy to read and test documentation framework, which is available to look at runtime, and allows generation of swagger documents at build time.

While the application is running: [Interactive docs](http://localhost:8080/swagger-ui/index.html)

