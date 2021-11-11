# Release 3

## Feedback from deliverable 2

### Refactoring and cleaning up local storage

As pointed out in the feedback we received on the second deliverable, we realized that we did not fully utilize the potential of the abstract class `Storage` and `Storable` interface. Hence, we decided to move the logic of the `Storage` class into [`CognitionStorage`](../../cognition/core/src/main/java/json/CognitionStorage.java), and deleted `Storable` and `Storage` entirely.

For the `cognition` code base, we see this as a cleaner solution, as we only need a single class for persistent storage.

### Overriding `toString()` and testing

The feedback for deliverable 2 stated that our `toString()` methods in our Java models (i.e. [`User`](../../cognition/core/src/main/java/core/User.java), [`Quiz`](../../cognition/core/src/main/java/core/Quiz.java) and [`Flashcard`](../../cognition/core/src/main/java/core/Flashcard.java)) could not guarantee a given format.

All the `toString()` methods in our Java models are overridden using the `@Override` annotation with a deterministic String representation.

Although testing these methods may seem redundant and bloated, we test these `toString()` methods in order to increase test coverage.

### UUID

We got feedback on the usage of UUID in the User class, as username is unique. We agree on this
and refactored the codebase to use username.

For Quiz and Flashcard we still use UUID as we do not want to force uniqueness in the quiz name and flashcard name. We feel that
this is a clean solution which makes for a good developer experience for a low cost.

### Encapsulation

We got multiple comments on encapsulation problems. One of these was getters that returned
internal lists, which we resolved by returning new lists with the same contents. The Controller
class also had three methods that we changed to private.

## Reflection

### Refactoring local storage

Taking a chronological view on the progress of our project, we added [`CognitionStorage`](../../cognition/core/src/main/java/json/CognitionStorage.java), the abstract `Storage` class and the `Storable` interface at the same time. For deliverable 1 and 2, we went on interacting with the local storage using only the `CognitionStorage` class, not utilizing the full potential of the hierarchy we had sketched out. This class hierarchy would have been more fitting if we had several implementing storage classes, but there was no need for this, due to the nature of our Java models.

In hindsight, we should have started with only the `CognitionStorage` class, and then later improved on the persistence solution. **That is more akin to agile software development**; continuously improving upon a working solution.

### Commit Culture

During deliverable 3, we put a great focus on the commit culture when contributing to our code base. Please see [CONTRIBUTING](../../CONTRIBUTING.md) for more information on contributing to the code base.

Throughout deliverable 1 and 2, we practised our own guideline on how to commit code to our code base. This is inline with the group contract formulated at the start of the project. The majority of these commits were only commit titles; rarely descriptions and footers. This was sufficient for the four of us in the group, but **we recognize that this is not sustainable for new developers** contributing to the code base.

Thus, we put more effort into every single commit during deliverable 3. Our commit messages now have a category (e.g. `feat`, `fix`, `docs`, etc...), title, description with **what** and **why**, and a reference to the issue to resolve. Branch names are clear and concise, and is to be marked with the ID number of the issue it resolves.
Example: `#10/feat-frontend`.

### Public methods in REST controller

In `CognitionController`, we have intentionally left all route functions as public, even though 
the server technically won't break if they are private. We have done this for three reasons:
1. The methods are logically public, as they are used when ui sends HTTP requests to it. 
2. The class is not being instantiated anywhere, and therefore the access modifiers do not matter
3. It makes it possible to unit test them
 
Public route methods are also the convention in Spring Boot, so doing this another way would 
confuse someone familiar with Spring Boot.
## REST server documentation

[Click here](../../cognition/api/src/main/asciidoc/index.adoc) to read the REST server documentation.

## Diagram documentation

[Click here](./DIAGRAMS.md) to view the documentation for various diagrams.

## `RemoteCognitionAccess`: The middle-man between `ui` and `api`

[`RemoteCognitionAccess`](../../cognition/ui/src/main/java/ui/RemoteCognitionAccess.java) is the middle-man between `ui` and `api`; the class responsible for handling HTTP queries between classes in the `ui` module and the `api` module.

## Updating the way a developer runs the application (using `make`)

When finishing deliverable 2 of this project, you could install dependencies and test the application using `mvn clean install`. If that command built successfully, you could run the application using `cd ui && mvn javafx:run`.

For deliverable 3, we converted the application to interact with a web server using the Spring Boot framework. This demanded that we reworked how the tests and the application is run, as there are more moving parts currently.

First, you need to **install dependencies without running tests**, in order to ensure that all Maven modules have downloaded necessary dependencies.

Then, you need to **test** the application. For the user interface tests to pass, this requires using a Spring Boot server running on our port for testing (port `3000`).

**Running the application** also requires a Spring Boot server to be running, this time on port `8080`. We made the choice of not running the test server and the application server on the same port in order to prevent potential crashes.

In summary, all these moving parts led us to streamline the process of testing and running the application. Hence, we added a [`Makefile`](../../cognition/Makefile) as a wrapper for the needed `mvn` commands to improve the quality of life for the current developers and "future" developer (the one grading this project). **We underline that we still use Maven for building the application.** Make is simply acts as an abstraction above the existing Maven commands.

Given the number of actual commands to run the application, the group saw this abstraction as necessary and worth dedicating time to, in order to improve the quality of life for the developer.

Please see the root [`README`](../../README.md) for more information on running the application, preferably using `make`.

**TL;DR** - You can now run tests using `make test` and run the application using `make` in the `cognition` directory.

## Default quizzes

Previously, new users started with no quizzes. Now, every new user is created with a standard
introductory quiz. By doing this, the user can now quickly use all available functionality in
Cognition immediately after registering.

We struggled a bit with where this logic should be located. First, we thought about putting it in
the `core` module, as this module is where the logic for writing new users to file is located. We
eventually ended up putting it in the `ui` module, specifically in the `RegisterController` class. The introductory quiz is now added client-side when the user is to be registered.

## Balancing implementing new functionality and ensuring code quality

After finishing deliverable 2, the Cognition application was a working minimum viable product (MVP). Due to an extended deadline for deliverable 2, the group had time to add the following extra functionality:

- Filter quizzes based on user input

Throughout the milestones for deliverable 3, the group has added the following extra features:

- Improve user experience when navigating through a quiz

- Add default quizzes to each user, in order for the user to quickly use all available functionality in Cognition

Furthermore, we have fixed suggested changes from deliverable 2.

We also felt the need to improve upon parts of our code base. This is in line with **agile development**; continuously improving existing code in our code base. Specifically, we improved JavaDocs, updated access modifiers where it was needed and increased readability in our code base.

**In summary, we feel that we struck a great balance between adding new functionality and improving code quality in our code base.**
