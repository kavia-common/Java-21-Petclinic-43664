# mybank (Java 21, Spring Boot 3.x)

my-bank REST API migrated to Java 21 and Spring Boot 3.x and integrated into this repository. The application exposes simple bank account operations backed by an in-memory H2 database seeded via `data.sql`. Swagger/OpenAPI and Actuator health are enabled.

Requirements:
- Java 21 (ensure JAVA_HOME points to a Java 21 installation or that `java -version` reports 21)
- The Maven Wrapper is included; you do not need Maven installed globally.

Build (recommended):
- Unix/macOS:
  ./mvnw -v
  ./mvnw -DskipTests clean package
- Windows:
  .\mvnw.cmd -v
  .\mvnw.cmd -DskipTests clean package

Troubleshooting Maven Wrapper:
- If you see: "Error: Could not find or load main class #", it is caused by lines in `.mvn/jvm.config` being passed as JVM arguments by the Windows wrapper.
  To prevent this, `.mvn/jvm.config` in this project contains no commented lines—only valid JVM flags are allowed if you decide to add any.
- Ensure the `mvnw` script:
  - starts with a Unix shebang `#!/bin/sh`
  - uses LF line endings
  - is executable: `chmod +x mvnw`
- Maven version:
  - The Maven Wrapper is configured to use Maven 3.9.x (or newer).
- If downloads are blocked by your network, try again when connectivity is available.

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
