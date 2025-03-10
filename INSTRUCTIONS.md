# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created for hiring needs of our company but can be used for other purposes.


## Project Setup and Instructions

### 1. Introduction

This is a Spring Boot-based application that exposes REST APIs for managing audition posts and their associated comments. The application includes integrations with monitoring tools like OpenTelemetry, logging via SLF4J, and uses several Gradle plugins for enhanced functionality.

**Main Features:**
- REST APIs for CRUD operations on posts and comments.
- OpenTelemetry integration for tracing and monitoring.
- Swagger UI for API documentation.
- Validation and exception handling.

### 2. Prerequisites

Before running the project, ensure you have the following installed:

- Java 17 (or higher)
- Gradle (Version 7.0+ recommended)
- Git (for version control)

### 3. Project Setup

**Clone the repository**

git clone https://your-repository-url.git  
cd your-project-directory

**Build the project**

Run the following command to build the application using Gradle:

./gradlew build

This will resolve the dependencies and compile the project.

**Run the application**

After building the project, you can run the Spring Boot application using the following command:

./gradlew bootRun

The application will start on [http://localhost:8080](http://localhost:8080).

### 4. How to Use the Application

**Available Endpoints**

- **Get all posts (optional filter by userId)**

  GET /posts?userId={userId}

  Retrieves all posts, optionally filtered by `userId`.

- **Get post by postId**

  GET /posts/{id}

  Retrieves a post based on the `postId`. If the `postId` is invalid or blank, a 400 Bad Request is returned.

- **Get comments by postId**

  GET /posts/{postId}/comments

  Retrieves all comments associated with a given post.

- **Get comments based on postId via request parameter**

  GET /comments?postId={postId}

  Retrieves comments for a given post by `postId`.

### 5. Testing

**Unit Tests**

To run unit tests, use the following command:

./gradlew test

This will execute all the unit tests, including validation checks and logic within your controller and service layer.

**Code Coverage**

Jacoco is integrated to generate code coverage reports. After running the tests, you can view the coverage reports:

./gradlew jacocoTestReport

This will generate a report in the `build/reports/jacoco/test/html/index.html` file.

### 6. Code Analysis

- **SpotBugs**

SpotBugs is configured for static code analysis. To run it, use:

./gradlew spotbugsMain

This will generate a report of potential bugs in the code.

- **Checkstyle**

To check the code for style violations, use:

./gradlew checkstyleMain

This will check your code against the defined coding standards.

---

