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

Troubleshooting Maven Wrapper:
- If you see: "Error: Could not find or load main class #", it was caused by comment lines in `.mvn/jvm.config` being passed as arguments to `java`.
  The `mvnw` script in this project has been fixed to ignore comment and blank lines in `.mvn/jvm.config`.
- Ensure the `mvnw` script:
  - starts with a Unix shebang `#!/bin/sh`
  - uses LF line endings
  - is executable: `chmod +x mvnw`
- If downloads are blocked by your network, try again when connectivity is available.

Alternative run script (fallback):
- Unix/macOS:
  ./run.sh                      # builds (if possible) and runs the jar with java -jar
  ./run.sh -Dserver.port=3002 -Dserver.address=0.0.0.0
  Notes:
  - The script tries `./mvnw`, then system `mvn`. If neither works but a jar exists in `target/`, it will run it.
  - Requires Java 21 available on PATH.
- Windows:
  Use the Maven Wrapper (`.\mvnw.cmd spring-boot:run`) or package and run the jar: `java -jar target\<artifact>.jar`.

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
