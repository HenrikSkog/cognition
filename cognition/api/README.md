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
