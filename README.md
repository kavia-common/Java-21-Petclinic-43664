# Java-21-Petclinic

A Java 21 Spring Boot 3.2.x Petclinic-style demo with Actuator health checks and Swagger UI (springdoc-openapi).

Requirements:
- Java 21 (ensure JAVA_HOME points to a Java 21 installation or that `java -version` reports 21)
- The Maven Wrapper is included; you do not need Maven installed globally.

Build:
- Unix/macOS:
  ./mvnw -v
  ./mvnw clean package
- Windows:
  .\mvnw.cmd -v
  .\mvnw.cmd clean package

Run (development):
- Default port (8080):
  - Unix/macOS:
    ./mvnw spring-boot:run
  - Windows:
    .\mvnw.cmd spring-boot:run
- Custom port 3002:
  - Unix/macOS:
    ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"
  - Windows:
    .\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"

Endpoints:
- Swagger UI: http://localhost:8080/swagger-ui/index.html (or /swagger-ui/index.html on your chosen port)
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Health (Actuator): http://localhost:8080/actuator/health
- Healthz (simple controller): http://localhost:8080/healthz
- Sample API:
  - GET /api/owners
  - GET /api/owners/{id}

Notes:
- Actuator exposes health and info: see src/main/resources/application.properties.
- Health probes (liveness/readiness) are enabled via management.endpoint.health.probes.enabled=true.
- This project is configured for Java 21 using maven-compiler-plugin and Spring Boot 3.2.x.
