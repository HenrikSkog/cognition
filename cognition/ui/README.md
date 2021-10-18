# UI module (frontend)

The module `cognition/ui` handles the user interface of the app. It uses the library [JavaFX](https://openjfx.io/) to
create an interactive graphical interface.

## Group deliverable 1: Current functionality

So far, the user interface consists of a login screen and a dashboard with a list view of stored flashcards.

As the complete user system has not yet been implemented, you sign in with the username "admin" and password "password".
If the user sign in with the correct credentials, they are redirected to the dashboard. If the credentials are not
correct, they are prompted with an error message. The flashcards on the dashboard are read from a file stored at
local `~` directory.

This `ui` functionality works with the `core` functionality connected to
issue [#12](https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2103/gr2103/-/issues/12) on GitLab, i.e. persistent
storage. This issue is also one of our main user stories.

## Group deliverable 2: Current functionality

This sprint has seen lots of UI improvements.

As opposed to the last delivery, users are now able to both sign up and sign in to their user, enabled by persistent
storage. Therefore, we have added a register page in addition to the login page from the first delivery.

The dashboard has been greatly improved by implementing several design elements from our Figma sketches. The design of
the dashboard is the main template for future pages, and has been implemented in the "Create Quiz" page.

There were a few challenges, for example being able to dynamically render a user's username on the dashboard page, as
the user is not yet set when the `initialize()` method is run. The solution ended up being to make the FXML `Text`
node `public`. We then access it and set it dynamically inside the `LoginController` before the scene is actually
rendered.
