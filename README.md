# Lilach
This is a project we're building as part of a software engineering course.

The system is splitted into two parts:

* **lilach-client:** The client side, built with JavaFX
* **lilach-server** The server side, built with MySQL, JavaFX and other libraries (TBA).

## Requirements
1. [JDK 13][https://openjdk.java.net/projects/jdk/13/]. We are using OpenJDK but the proprietary one should also work fine.
2. [Maven][https://maven.apache.org/].
3. [MySQL][https://www.mysql.com/]. MariaDB should also work fine but is not guaranteed to work.

## Running the project
We are using [Maven][https://maven.apache.org/] as our build system. So, in order to run the whole project you have three options:

1. **Running using IntelliJ IDEA**

   This can be achieved by simply using the *Client and Server* compound configuration.

2. **Running using two separate commands**

   Simply open two terminal windows, one in the *lilach-client* directory and another in the *lilach-server* directory, and on each of them type `mvn javafx:run`.

3. **Running using one command**

   Note: You will get an error after running, since the parent project can not handle the *javafx:run* task itself. Maybe we will fix that, but it isn't a top priority.
   
   Just run: `mvn -T 2 javafx:run`.

## Team Guidelines

* When in doubt, ask, but try to find a solution first.
* Use the [Clean Code Summary][https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29] as a guideline.
* You should work in at least one feature branch. Changes will only be merged using pull requests.
* Reuse as much as you can (but pay attention: **None** of the stuff provided in the labs is allowed!)
* Wit beyond measure is man’s greatest treasure. (This is for Yaron)
* Try to have fun :)
