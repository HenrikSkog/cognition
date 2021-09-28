# UI module (frontend)

The module `cognition/ui` handles the user interface of the app. It applies the library [JavaFX](https://openjfx.io/) to
create an interactive graphical interface.

## Current functionality
So far, the user interface consists of a login screen and a dashboard with a list view of stored flashcards. 

As the complete user system has not yet been implemented, you sign in with the username "admin" and password "password". If the user sign in with the correct credentials, they are redirected to the dashboard. If the credentials are not correct, they are prompted with an error message. The flashcards on the dashboard are read from a file stored at local `~` directory.

This is progress on issue [#12](https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2103/gr2103/-/issues/12) on GitLab. This issue is also one of our main user stories.