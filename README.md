# I must
"I must" allows you to take notes, set reminders and store Images while also offering online synchronization to avoid losing all of your appointments.

You can create an user account that allows you to synchronize your tasks. A task has a description which can be spoken through Speech to text function. It also has *more options* in case you want to add more information to it, such as an alarm notification and the capacity to store images to help remembering what you must do.

![I must](https://github.com/gabrielborgesdm/todo-app/blob/main/screenshots/screenshots.png)

## Publishing details
- API docs available [here](https://documenter.getpostman.com/view/6190871/TzY4eaE9 "here")

## Application details
**API Server**
- API Rest that uses the following technologies *Node.Js*, Express, *Docker*, *Docker Compose*,  *Eslint*, *Typescript*, *Mongoose* and *Joi*  and *JWT*.
- It was developed following an adapted version of the *MVC architecture*. Here, the *Model* is only for *schemas* and *models*, while the *Controller* holds all of the server logic.
- It also has helpers with useful functions to be used by the *Controller*
- The route system was developed using express, it has two middlewares.
	- *Auth Middleware*: Check if the *JWT token* is valid.
	- Validation Middleware: Check if the request's body is valid through the *JOI* validation library. Routes have specific schemas responsible for checking if the user is sending the right data.
- It use Mongo database to store the server data. *Mongoose* was chosen as the ODM package.
- For deployment there's a *docker-compose* and *Dockerfile* files that helps building and starting the server.

**Mobile application**
- *Java* application using *Model–view–viewmodel* architecture.
- Offline first application with server side syncronization.
- *Realm database* for offline data, it uses *Singleton data pattern* to avoid performance issues.
- *Material Design* for a better layout.
- *Retrofit API Interface* in order to do the server requests.
- *PhotoView* to help zooming and displaying images.

## Installation Details
**API Server**
- Make sure you have [docker](https://docs.docker.com/engine/install "docker") and [docker-compose](https://docs.docker.com/compose/install/ "docker-compose") installed.
- Rename the *.env-test* file to *.env* and replace its content with your environment variables.
- Run the following command to start the server: `sudo docker-compose up`.

**Mobile application**
- Make sure you have [Android Studio](https://developer.android.com/studio "Android Studio") installed.
- Open the mobile app with it.
- Open the *build.gradle* file and change *BuildConfig* variables with your configuration.
