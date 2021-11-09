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

Make sure you have Maven installed and can successfully use `mvn` in your terminal. Launching the application using Gitpod should automatically solve this issue for you. If you are on Mac or Linux, simply open the terminal and write `brew install maven` if you do not have Maven installed locally yet.

### Important to note

Before running the application, let's add some context as to why we have 2 solutions. When testing the application, the `ui` requires a running Spring Boot server (port `3000`). The same goes for running the application; the `ui` requires a running Spring Boot server (port `8000`). This causes the number of commands in the command line to increase drastically. Hence, we added a [`Makefile`](./cognition/Makefile) as a wrapper for the needed `mvn` commands to improve the quality of life for the current developers and "future" developer (the one grading this project).

### Using `make` with `mvn` under the hood **(recommended)**

```sh
# Navigate to the cognition directory.
cd cognition

# Install dependencies, start application server, and start client application.
# Alternatively, "make app" does the same.
make

# Install dependencies, start test server, and run tests for all modules.
make test

# Print the available targets.
make help
```

**`make test` and `make app` are all that is needed to run tests and the Cognition application.**

Please see the [`Makefile`](./cognition/Makefile) for more information on the targets we have made.

### Using `mvn`

```shell
# Navigate to the cognition directory.
cd cognition

# Install dependencies without running tests.
# Required to run before tests to ensure all dependencies are loaded.
mvn clean install -DskipTests

# Start Spring Boot on port for testing.
cd api && mvn spring-boot:run -Dspring-boot.run.arguments=testmode

# Run tests for all Maven modules.
cd cognition && mvn test

# Start Spring Boot server on port for application logic.
cd api && mvn spring-boot:run

# Run the client application.
cd ui && mvn javafx:run
```

**As seen in the commands above, the `make` option is clearly quicker and more pleasant for the developer.**
