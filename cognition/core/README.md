# Core module

## Core package

The [`core`](src/main/java/core) package consists of plain old Java object (POJOs). These objects are responsible for
the business logic of Cognition.

For more documentation of each class in this package, please see their respective Javadoc. All classes are thoroughly
documented.

## JSON package

### File stucture for local storage

The group have chosen JSON as the file format for storing data locally. The logic for this local file storage is located
in the [`json`](src/main/java/json) package in the [`core`]((src/main/java/core)) module.

The locally stored `.json` files are located at `[user.home]/it1901-gr2103/cognition`. This path is different for Unix
and Windows. If you are on a Unix system (Mac or Linux), you can use the following command in your terminal to navigate
to the locally stored data:

```sh
# If you are using Mac or Linux, navigate to the locally stored .json files
cd ~/it1901-gr2103/cognition

# List all files in this directory
ls
```

Please note that you must adapt the path if you're using Windows.

We use different files for the application persistence and the persistence during testing. As an example, `users.json` is created for the application logic, whilst `cognitionTest.json` is created for testing.

### Storage Format

Currently, all of the data is stored in the `users.json` file. The storage format is as follows:

```json
[
  {
    "uuid": "ce0d1036-d5be-4604-90ff-fa8472050da0",
    "username": "username-1",
    "password": "password",
    "quizzes": [
      {
        "id": "69448785-d05c-43c2-9fe6-03ae23cca595",
        "name": "quiz-name",
        "description": "Description of the quiz",
        "flashcards": [
          {
            "uuid": "b0b53bc8-de4e-4686-b4e1-f19df38477cb",
            "front": "front0",
            "answer": "answer0"
          },
          {
            "uuid": "8163a2bd-7614-4ce5-811c-2ce09b80b5bc",
            "front": "front1",
            "answer": "answer1"
          },
          {
            "uuid": "9bcc3763-0982-457d-a1a5-1ef9ef7608cb",
            "front": "front2",
            "answer": "answer2"
          }
        ]
      }
    ]
  },
 ...
]
```

### Classes

The `json` package includes classes responsible for serializing Java objects to JSON data, and deserializing JSON data
into Java objects. [Gson](https://github.com/google/gson) is used to achieve this. Gson is a robust library for
converting Java objects into JSON data and vice versa, developed by Google.

[`Storable`](src/main/java/json/Storable.java) is an interface that provides the four basic functions of persistent
storage: Create, read, update and delete. [`Storage`](src/main/java/json/Storage.java) is an abstract class that
implements [`Storable`](src/main/java/json/Storable.java), and includes the shared logic of our serializers /
deserializers. The extensions of [`Storable`](src/main/java/json/Storable.java),
i.e. [`UserStorage`](src/main/java/json/UserStorage.java) and [`QuizStorage`](src/main/java/json/QuizStorage.java), are
responsible for persistently storing the plain old Java objects in our application.

When serializing and deserializing Java objects, Gson needs to know the type of object (
e.g. [`Flashcard`](src/main/java/core/Flashcard.java)) to handle. Thus, we need extensions
of [`Storage`](src/main/java/json/Storage.java) for each model, rather than one shared serializer and deserializer.

For more documentation of each class in this package, please see their respective Javadoc. All classes are thoroughly
documented.
