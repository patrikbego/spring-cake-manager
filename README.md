# 🍰 Cake Manager Microservice

This project is a simple Spring Boot application built with Gradle. It supports full CRUD operations, pagination, validation, authentication, caching, and documentation. Continuous integration is configured using GitHub Actions via .github/workflows/ci.yml.

---

## 🚀 Features

* ✅ RESTful API for managing cakes (`/api/v1/cakes`)
* ✅ Pagination, sorting, and filtering support
* ✅ DTO-based API design using Java Records
* ✅ Input validation with `jakarta.validation`
* ✅ Swagger/OpenAPI 3 for API documentation
* ✅ Caching with Caffeine
* ✅ Basic authentication with role-based access
* ✅ Graceful error handling
* ✅ Logging via SLF4J
* ✅ H2 in-memory database with initial data seeding
* ✅ Docker-ready (Dockerfile included)
* ✅ Ready for CI integration (GitHub Actions)

---

## 🧱 Tech Stack

* Java 21
* Spring Boot 3.5.x
* Spring Data JPA
* H2 Database
* Spring Security (Basic Auth)
* OpenAPI/Swagger (springdoc-openapi)
* Caffeine Cache
* Gradle (Kotlin DSL)

---

## 🔧 Running the Application

### Prerequisites

* Java 21+
* Gradle or use `./gradlew`

### Build

```bash
./gradlew build
```

The runnable JAR will be created in `build/libs/`.

You can run the jar directly:

```bash
java -jar build/libs/spring-cake-manager-0.0.1-SNAPSHOT.jar
```

### Start the App

```bash
./gradlew bootRun
```
### Start the App with Docker

```bash
docker build -t spring-cake-manager .
docker run -p 8080:8080 spring-cake-manager
```

App runs at: [http://localhost:8080](http://localhost:8080)

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 Authentication

Basic Auth is enabled. Credentials are loaded from environment variables with
defaults provided in `application-dev.properties`.
You can override them using the following variables:

* `ADMIN_USERNAME` / `ADMIN_PASSWORD`
* `USER_USERNAME` / `USER_PASSWORD`

The default `dev` profile defines these users:

| Username | Password   | Role  |
| -------- | ---------- | ----- |
| `admin`  | `admin123` | ADMIN |
| `user`   | `user123`  | USER  |

When running tests (`test` profile) the credentials are `testadmin`/`testadmin123`
and `testuser`/`testuser123`.

* `USER` can **read** cakes.
* `ADMIN` can **create, update, delete** cakes.

---

## 🧪 Testing

Run tests using:

```bash
./gradlew test
```

---

## 🧾 API Endpoints

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| GET    | `/api/v1/cakes`      | List all cakes (paginated) |
| GET    | `/api/v1/cakes/{id}` | Get a cake by ID           |
| POST   | `/api/v1/cakes`      | Create a new cake          |
| PUT    | `/api/v1/cakes/{id}` | Update an existing cake    |
| DELETE | `/api/v1/cakes/{id}` | Delete a cake              |

Detailed api docs can be found here: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
---

## 📦 Caching

* List and individual cake lookups are cached using Caffeine.
* Cache is invalidated on create, update, and delete operations.

---

## 📂 Project Structure

```
src/main/java/org/example/springcakemanager
├── controller      # REST Controllers
├── dto             # CakeDTO record
├── model           # JPA Entity (CakeEntity)
├── repository      # Spring Data Repository
├── service         # Business logic
├── config          # Security, Caching, Swagger config
```
## ⚙️CI workflow

Whenever you push commits to GitHub or open a pull request, GitHub Actions will
automatically run the workflow. The workflow performs the following steps:

1. **Checkout the repository** – Retrieves your code.
2. **Set up JDK 21** – Installs a Java 21 environment with caching for Gradle
   dependencies.
3. **Grant execute permission** – Ensures the Gradle wrapper script can run.
4. **Build with Gradle** – Executes `./gradlew build` which compiles the
   application and runs the tests.

The results appear in the "Actions" tab and as status checks on your pull
requests.

## Running the workflow

You don't need to start the workflow manually. Simply push your changes to a
branch or open a pull request on GitHub and the workflow will run
automatically. To test locally before pushing, run:

```bash
./gradlew build
```

This performs the same build and test steps as the workflow.
