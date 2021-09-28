# IT1901 - Group 21-03

[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2103/gr2103)

## Description

Cognition is - plain and simple - a flashcard application. If you've used [Quizlet](https://quizlet.com/), Cognition
should feel quite familiar.

The Java project is located inside the `cognition` directory. [Click here](./cognition/README.md) to read to project description.

## Deliverable Documentation

The documentation for each deliverable is located inside the `docs` directory. [Click here](./docs) to read the deliverable documentation.

## Developer Information

Developed by:

- Magnus Rødseth
- Julian Grande
- Thomas Hasvold
- Henrik Skog

## Tech Stack

- JavaFX
- Java
- Maven

## Running the application

Make sure you have Maven installed and can successfully use `mvn` in your terminal. Launching the application using
Gitpod should automatically solve this issue for you.

```shell
# Navigate to the cognition directory
cd cognition

# Install dependencies and run all tests
mvn clean install

# Run Spring Boot server (after running mvn install)
mvn spring-boot:run -f api/pom.xml

# Run frontend (after running mvn install)
mvn javafx:run -f ui/pom.xml
```
