# API module

## Description

The API for persistent storage is located in the module `cognition/api`. The API uses
the [Spring Boot](https://spring.io/projects/spring-boot) framework to create a web REST API. Additionally, Spring Boot
has robust serialization and deserialization of _Plain Old Java Objects_ (POJOs),
using [Jackson](https://github.com/FasterXML/jackson) under the hood.

## Future implementation

Internally, the Spring Boot server will use the [MongoDB API](https://docs.mongodb.com/drivers/java/sync/current/) to
connect to a remote MongoDB database. This allows the entire group to share the same data set whilst developing, making
it easier to collaborate on new functionality and saving time.
