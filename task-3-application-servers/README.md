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
when the application starts, add the data in the file 
[`DatabaseSetup`](src/main/java/com/felixseifert/kth/networkprogramming/task3/databaseconnection/DatabaseSetup.java) 
as this is implemented as a context listener and is executed automatically when the application starts.

## Add Questions

Questions can be added under the relative URL `/add`. The question needs four answers and at least one of them has to be 
marked as correct. As the user interface and experience was not considered, the different links are only accessible via 
the URL field of a browser and the design is not very appealing.

## Database Connection and Handling

As a database, we selected an H2 in-memory database. As we did not use any Object Relational Mapper (ORM) like 
Hibernate, we used raw Java Database Connection (JDBC). The file 
[`DatabaseSetup`](src/main/java/com/felixseifert/kth/networkprogramming/task3/databaseconnection/DatabaseSetup.java) 
loads the H2 driver class and creates the tables of the entities which should be stored in the database.

The entity classes have to have information about the desired SQL table and the SQL column names. In addition, the SQL 
characteristics of its columns are also stored there.

The interaction between the business logic and the database happens via the repository layer and decouples the database 
from the business layer. If an entity class requires changes (e.g. new attribute or change of an existing attribute), 
all the changes needed should happen only within the entity class and a change of the respective repository class or the 
setup should not be needed. To achieve this, each repository creates its database queries and commands based on the 
information provided by the entity class. To abstract this further and allow the re-usability of the code for other 
repositories, the class 
[`RepositoryUtils`](src/main/java/com/felixseifert/kth/networkprogramming/task3/databaseconnection/DatabaseUtils.java) 
bundles the methods for query and command creation.

## Deploy With Docker

Firstly, you would have to build the application with Maven.

```
mvn package
```

The build process creates a `.war` file. The most portable way is to create a Docker image with the Tomcat server and 
the application file. 

```
docker build -t tomcat-quiz .
```

Once the Docker image is correctly built, you can run it locally.

```
docker run -p 8080:8080 tomcat-quiz
```

The started application is available under the URL

```
http://localhost:8080/task-3-application-servers-1.0-SNAPSHOT
```

In addition, the code of the main branch is available as a docker image online and can be pulled and run from the 
Docker registry.

```
docker run -p 8080:8080 seifertfelix/tomcat-quiz
```