# SoniQ Cleaner Assignments App

## Setup

### Requirements
 * `JAVA_HOME` set to Java 8, as requested.
 * Gradle 5+ (included.)

### Running

The application can be started with either gradle or docker.

When started, a REST API will be exposed on http://localhost:8080

#### Gradle
    
From the root directory, run:
 
 `soniq-app$ ./gradlew bootRun`

#### Docker
 
From the root directory, run: 

    soniq-app$ docker build --tag app .
    soniq-app$ docker run --interactive --tty --rm --publish 8080:8080 app

## Design

#### Additional libraries

##### [Lombok](https://projectlombok.org/)

I decided to take the opportunity to play with project lombok, 
so there's a lot more annotations and less boiler plate. Builders, factory methods, 
getters and setters are all generated for free, and there's support for `var` and `val`.

I've been developing using IntelliJ, so had to use a plugin to get AST support.

#### [jOOλ](https://github.com/jOOQ/jOOL)

Just needed a `zip` function for streams, so pulled in this library for tests.

### Rest API

There are a single resources exposed via REST:

#### [/assignments](http://localhost:8080/assignments)

 * `POST`: `/assignments`, see [InputDto](src/main/java/io/mjmoore/dto/InputDto.java)

### Application design

The structure of the project is relatively straight forward:

 * A controller sits at the front, exposing data via a REST path.

   Incoming data is accepted as a [DTO](src/main/java/io/mjmoore/dto/InputDto.java)

 * The controller will call a service, which will process the data and return 
   a suggested list of assignments.
   

### Testing

Tests can be run using gradle. From the root directory, run: 

`pinguin-app$ ./gradlew test`

Tests are explicitly run as part of a build.

Tests related to assignment logic can be found in 
[AssignmentCalculatorTest](src/test/java/io/mjmoore/service/AssignmentCalculatorTest.java)
and are probably the most interesting.

#### Erratum 

I spotted a small mistake in the sample data.

Output format is:

    [ 
        {senior: 3, junior: 1}, 
        {senior: 1, junior: 2}, 
        {senior: 2, junior: 0}, 
        {senior: 1, junior: 3} 
    ]
    
Which should be:

    [ 
        {"senior": 3, "junior": 1}, 
        {"senior": 1, "junior": 2}, 
        {"senior": 2, "junior": 0}, 
        {"senior": 1, "junior": 3} 
    ]