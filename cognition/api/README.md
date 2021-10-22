# API module

## Description

The API for persistent storage is located in the module `cognition/api`. The API uses
the [Spring Boot](https://spring.io/projects/spring-boot) framework to create a web REST API. Additionally, Spring Boot
has robust serialization and deserialization of _Plain Old Java Objects_ (POJOs),
using [Jackson](https://github.com/FasterXML/jackson) under the hood.

## Future implementation

Internally, the Spring Boot server will use the local persistent storage solution in the `core` module.
