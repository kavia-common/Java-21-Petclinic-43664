# mybank (Java 21, Spring Boot 3.x)

my-bank REST API migrated to Java 21 and Spring Boot 3.x and integrated into this repository. The application exposes simple bank account operations backed by an in-memory H2 database seeded via `data.sql`. Swagger/OpenAPI and Actuator health are enabled.

Requirements:
- Java 21 (ensure JAVA_HOME points to a Java 21 installation or that `java -version` reports 21)
- Use the Maven Wrapper; you do NOT need Maven installed globally.

Build (recommended):
- Unix/macOS:
  ./mvnw -v
  ./mvnw -DskipTests clean package
- Windows:
  .\mvnw.cmd -v
  .\mvnw.cmd -DskipTests clean package

Run (preferred via Maven Wrapper):
- Default port (8080):
  - Unix/macOS:
    ./mvnw spring-boot:run
  - Windows:
    .\mvnw.cmd spring-boot:run

- Custom port 3002 bound to 0.0.0.0 (recommended for container/preview):
  - Unix/macOS:
    ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.arguments="--server.port=3002,--server.address=0.0.0.0"
  - Windows:
    .\mvnw.cmd -q -DskipTests spring-boot:run -Dspring-boot.run.arguments="--server.port=3002,--server.address=0.0.0.0"

Notes about port 3002:
- If port 3002 is already in use, the application may fail to bind. This is a port conflict, not a wrapper issue.
- To verify the wrapper is working (no exit code 127), run: `./mvnw -v`. If that succeeds, the wrapper is functional.
- You can choose a different port, e.g.: `-Dspring-boot.run.arguments="--server.port=3100,--server.address=0.0.0.0"`

Troubleshooting Maven Wrapper (prevents exit code 127):
- Ensure the `mvnw` script:
  - starts with a Unix shebang `#!/bin/sh`
  - uses LF line endings
  - is executable: `chmod +x mvnw`
- The Maven Wrapper auto-downloads the correct Maven version based on `.mvn/wrapper/maven-wrapper.properties`.
- If downloads are blocked by your network, try again when connectivity is available.
- Verify wrapper works: `./mvnw -v` (no need for system `mvn`).

Java 21 selection and Maven configuration:
- This project compiles with Java 21 via maven-compiler-plugin `<release>21</release>`.
- Spring Boot 3.2.x is used and compatible with Java 21.
- If you encounter "release version 21 not supported", your runtime JDK is older than 21. Fix by selecting a Java 21 JDK:
  - Unix/macOS:
    - If using SDKMAN: `sdk use java 21.x.y-z`
    - Or set JAVA_HOME to a JDK 21 path, for example:
      export JAVA_HOME=/path/to/jdk-21
      export PATH="$JAVA_HOME/bin:$PATH"
    - Verify: `java -version` shows 21 (and `./mvnw -v` reports Java home pointing to JDK 21).
  - Windows (PowerShell):
    - Set JAVA_HOME to a JDK 21 dir and ensure `%JAVA_HOME%\bin` is first in PATH.
    - Verify: `java -version` shows 21.

Endpoints:
- Accounts:
  - GET  /accounts/all
  - GET  /accounts/{id}
  - POST /accounts/new
  - PUT  /accounts/{id}?amount=<double>
- Swagger UI:
  - Local: http://localhost:8080/swagger-ui/index.html
  - Shortcut: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Health (Actuator): http://localhost:8080/actuator/health
- Healthz (custom controller): http://localhost:8080/healthz

OpenAPI server URL (optional):
- Override the "servers" URL shown in Swagger UI using:
  - JVM property: `-Dmybank.openapi.server-url=https://<host>:<port>`
  - Environment variable: `MYBANK_OPENAPI_SERVER_URL=https://<host>:<port>`
- If not provided, defaults to http://localhost:8080.

H2 Console (optional):
- Console: http://localhost:8080/h2-console
- Default in-memory DB is auto-configured by Spring Boot when H2 is on the classpath.
- `data.sql` seeds initial records.

Notes:
- Actuator exposes health and info; health probes are enabled via `management.endpoint.health.probes.enabled=true`.
- This project is configured for Java 21 using maven-compiler-plugin and Spring Boot 3.2.x.
