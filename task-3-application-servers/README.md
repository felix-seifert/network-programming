# Task 3 - Application Servers

In this project we created a simple Java programme which runs on a Tomcat server. The programme builds upon the 
technologies of Java servlets, displays the frontend in the browser with Java Server Pages (JSP) and uses and H2 
in-memory database.

The main idea of the application is to present a quiz to the users. The quiz can have several questions and each 
question can have multiple correct answers. For accessing the quiz, the potential user has to login first. New users 
can be registered and authenticated users can also add new questions.

## Authenticate Users

After starting the server with the developed app, the index automatically forwards the user to the page of the quiz 
which is available under the relative URL `/quiz`. Only authenticated users can see the quiz (all user have the same 
authorisation). Unauthenticated users are forwarded to the login page under the relative URL `/login`.

To achieve this functionality, the login function sets the username as a session attribute after checking that the 
provided credentials are available in the database. Therefore, it is possible to check whether the user is 
authenticated via checking for the session attribute. The whole login process and the storage of data in the database 
is not secure and no communication or storage happens encrypted.

## Register Users

New users can be registered under the relative URL `/register`. The provided credentials get saved in the database and 
can then be used. However, the database exists only while the application is running. To persist data automatically 
when the application starts, add the data in the file `DatabaseSetup` as this is implemented as a context listener and 
is executed automatically when the application starts.

## Add Questions

Questions can be added under the relative URL `/add`. The question needs four answers and at least one of them has to be 
marked as correct. As the user interface and experience was not considered, the different links are only accessible via 
the URL field of a browser and the design is not very appealing.
