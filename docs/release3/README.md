# Release 3

## Feedback from deliverable 2

### Access modifiers in [`Controller`](../../cognition/ui/src/main/java/ui/controllers/Controller.java) class

We got feedback that a couple of methods in the Controller class should be made `protected` instead of `public`. The two methods switchScene and changeToView were made `protected` as suggested, but the third and final method, getLoader has to remain public as it is used in the UI-tests. 

### Refactoring and cleaning up local storage

As pointed out in the feedback we received on the second delivery, we realized that we did not fully utilize the potential of the abstract class `Storage` and `Storable` interface. We therefore decided to move the logic of the `Storage` class into `CognitionStorage`, and deleted `Storable` and `Storage` entirely.
For the Cognition code base, we see this as a cleaner solution as we only need one class for persistent storage.

## Reflection

### Refactoring and cleaning up local storage

> TODO: Reflect upon time use of adding Storable, Storage and CognitionStorage classes. We should have started with only CognitionStorage, and then later improved on the persistence. That is more akin to agile development. 