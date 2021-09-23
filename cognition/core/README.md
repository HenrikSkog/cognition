# Core module

## Core package

The module `cognition/core` consists of plain old Java object (POJOs). These objects are responsible for the business
logic of Cognition.

### [`Flashcard`](src/main/java/core/Flashcard.java)

`coginition/core` has a [`Flashcard`](src/main/java/core/Flashcard.java) class and a corresponding simple
test called [`FlashcardTest`](src/test/java/core/FlashcardTest.java). The [`Flashcard`](src/main/java/core/Flashcard.java) class will be responsible for storing the flashcard data.

### [`CognitionModel`](src/main/java/core/CognitionModel.java)

The `core` module also has [`CognitionModel`](src/main/java/core/CognitionModel.java) class. This class is responsible
for modelling the business logic of the application, currently used for local storage and later used in the REST
API. `CognitionModel` currently supports getting flashcards, putting flashcards and iterating through a collection of
flashcards. Please note that the model of the business logic is not finished, but rather tentative until further notice.

## JSON package

The `json` package includes classes responsible for serializing Java objects to JSON data, and deserializing JSON data
into Java objects. [Jackson](https://github.com/FasterXML/jackson) is used to achieve this.

### [`CognitionModule`](src/main/java/json/CognitionModule.java)

The [`CognitionModule`](src/main/java/json/CognitionModule.java) class configures serialization and deserialization of
CognitionModel objects. This is naturally a work in progress, as more serializers and deserializers will be added
throughout the project lifetime.

### [`CognitionPersistence`](src/main/java/json/CognitionPersistence.java)

The [`CognitionPersistence`](src/main/java/json/CognitionPersistence.java) is responsible for persistently storing JSON
data. Please inspect the `public static void main` method
in [`CognitionPersistence`](src/main/java/json/CognitionPersistence.java) to see a proof-of-concept test of successfully
storing and reading from a local JSON file. The generated file is saved to the users home directory. 

### [`FlashcardSerializer`](src/main/java/json/FlashcardSerializer.java) and [`FlashcardDeserializer`](src/main/java/json/FlashcardDeserializer.java)

[`FlashcardSerializer`](src/main/java/json/FlashcardSerializer.java)
and [`FlashcardDeserializer`](src/main/java/json/FlashcardDeserializer.java) are responsible for serializing and
deserializing the Flashcard objects.
