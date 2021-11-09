# Release 1

## Build using Maven

The `cognition` Java project uses Maven to build and run.

```sh
# Navigate to the cognition directory
cd cognition

# Install dependencies and run all tests
mvn install

# Run Spring Boot server (after running mvn install)
mvn spring-boot:run -f api/pom.xml

# Run frontend (after running mvn install)
mvn javafx:run -f ui/pom.xml
```

## Gitpod

The application can be developed and executed using Gitpod. Use the badge in the root [`REAMDE.md`](../../README.md) to
launch the application using Gitpod.

Each developer is encouraged to use their IDE of choice, as long as all functionality also supports using Gitpod.

## Contributing

[Click here](../../CONTRIBUTING.md) to read more about contributing to the code base.

This documentation includes running the application with GitPod, milestones, issue tracking and templates, labels, commits, GitLab boards, branches and merge requests.

## User Interface

Please see the [UI module documentation](../../cognition/ui/README.md) for thorough documentation of its classes.

## Core and persistent local storage

Please see the [Core module documentation](../../cognition/core/README.md) for thorough documentation of its classes.

## REST API

Please see the [API module documentation](../../cognition/api/README.md) for thorough documentation of its classes.

## Continuous Intergration (CI) and code quality

The pipeline for continuous integration must succeed before merging new functionality.

The code must compile, pass all code quality checks and pass all tests.

The following tools will be used to ensure code quality:

- [Checkstyle](https://checkstyle.sourceforge.io). Validates design, checks code layout and checks code formatting.

- [Spotbugs](https://spotbugs.github.io/). Highlights potential bugs in Java code.

- [Jacoco](https://www.jacoco.org/jacoco/). Gathers test information and displays code coverage.

## Testing

New functionality should - if appropriate (which it almost always is) - be tested before it is merged.

## Reflection

In short, deliverable 1 went quite smoothly. Setting up the multi-module Maven project was fine in itself. Specifying each module's dependencies and opening up packages to other modules was a bit challenging, but the group worked it out during pair programming with some trial and errors. In this case, Maven was quite strict with naming and directory structure.

Working with issue tracking and branches is something all 4 group members are comfortable with from before. Thus, the real challenge lies in scoping the issues correctly, i.e. not creating too large issues. For group deliverable 1, the group feels that this went very well. As mentioned in the deliverable documentation, we aim to scope each issue to take a maximum of 12 hours (See `### Scope of an issue`). In general, we feel that we accomplished this throughout the entire milestone of group deliverable 1.

Working with GitLab issue templates and GitLab merge request templates also helped us narrow down and specify each problem to solve. This was beneficial both for the developer and the code reviewer.

To summarize, group deliverable 1 mainly focused on laying down the groundwork for a pleasant Git workflow. There were some hiccups whilst setting up the multi-module Maven project, but that was quickly solved due to good teamwork. The group is happy with the current groundwork, and agrees that this will be easier to build upon in later stages.

## Design

Provisional design sketches showing the login-, home- and quiz-screen, as well as a contemporary design for the
flashcards themselves.

The tentative design sketches can be
found [here at Figma](https://www.figma.com/file/dlrynKyn3KHJIdElsM12CB/Cognition-Design?node-id=0%3A1).

### Login Screen

![Login Screen](img/login_screen.png)

### Home Screen

![Home Screen](img/home_screen.png)

### Taking a Quiz

![Quiz Screen](img/quiz_screen.png)

### Flashcard - Type 1 (Input)

![Flashcard - Type 1 (Input)](img/flashcard_1.png)

### Flashcard - Type 2 (Guess)

![Flashcard - Type 2 (Input)](img/flashcard_2_1.png)

![Flashcard - Type 2 (Input)](img/flashcard_2_2.png)
