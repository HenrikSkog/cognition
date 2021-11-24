# API module (web server application)

## Description

The API uses the [Spring Boot](https://spring.io/projects/spring-boot) framework to create a web server with a REST API. Additionally, Spring Boot
has robust serialization and deserialization of _Plain Old Java Objects_ (POJOs),
using [Jackson](https://github.com/FasterXML/jackson) under the hood.

## REST server documentation

[Click here](src/main/asciidoc/index.adoc) to read the REST server documentation.

## Testing

[The tests in the `api` module](src/test/java/api) test the REST controller in isolation, in addition to the endpoints served by the Spring Boot web server.

Please see the JavaDoc in the respective tests for more documentation on the tests for the `api` module.

## Diagrams

The `abstract` diagram helps a new developer get an overview of the relationships and roles of each class before reading the `detailed` version, which in essence, is the more familiar way of drawing class diagrams. We choose this way of presenting the modules as it helps new developers to the project quickly understand the purpose of each class and how that comes into play in the broader task of the module.

### Abstracted

![API Descriptive Diagram](../../docs/plantuml/release3/img/api_abstracted.png)

### Detailed

![API Class Diagram](../../docs/plantuml/release3/img/api_detailed.png)

> Does not include setter and getter methods as well as variables and functions that are not necesary to get an overview of the classes function and its relationship to other classes.
