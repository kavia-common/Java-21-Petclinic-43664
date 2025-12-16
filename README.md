# Java-21-Petclinic-43664 (Java 21, Swagger UI, H2)

This Spring Boot service mirrors the my-bank account REST API within the Java-21-Petclinic-43664 container using Java 21, H2, and Springdoc OpenAPI. Endpoints, entity, repository, exception handling, H2 configuration, and seed data are aligned to my-bank.

## Requirements
- Java 21 (LTS)
- No system Maven needed (Maven Wrapper included)

## Build and Run

Using Maven Wrapper:
- Run the app (tests enabled):
  ./mvnw spring-boot:run

- Run the app (skip tests for faster startup):
  ./mvnw -DskipTests spring-boot:run

The app starts at: http://localhost:8080

## Swagger/OpenAPI
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## H2 Database
- H2 Console: http://localhost:8080/h2-console
- In-memory DB with seed data from src/main/resources/data.sql
- JPA auto DDL, data.sql runs after schema init

## Endpoints (aligned with my-bank)
- GET /accounts/all
- GET /accounts/{id}
- POST /accounts/new
- PUT /accounts/{id}
  - Params: amount (double) as query param or numeric request body
- DELETE /accounts/{id}

## Package structure
- com.example (application entry)
- com.example.config (OpenAPI metadata)
- com.example.mybank (parallel namespace mirroring my-bank)
  - controller.AccountController
  - model.Account
  - repository.AccountRepository
  - exception.ResourceNotFoundException

## Notes
- Java 21 compiler release is enforced in pom.xml.
- Springdoc OpenAPI starter provides Swagger UI at /swagger-ui.html.
- H2 is configured in application.properties and preloaded via data.sql.

