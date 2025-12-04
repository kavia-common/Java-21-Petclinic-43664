# Java-21-Petclinic

A Java 21 Spring Boot 3.2.x Petclinic-style demo with Actuator health checks and Swagger UI (springdoc-openapi).

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
  - The Maven Wrapper is configured via `.mvn/wrapper/maven-wrapper.properties` to use Maven 3.9.x (or newer).
- If downloads are blocked by your network, try again when connectivity is available.

Java 21 selection and Maven configuration:
- This project compiles with Java 21 via maven-compiler-plugin `<release>21</release>`.
- Spring Boot 3.2.x is used and compatible with Java 21.
- The Maven Wrapper uses Maven 3.9.x (see `.mvn/wrapper/maven-wrapper.properties`).
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
- Toolchains (optional but supported):
  - If your environment uses Maven Toolchains, this project includes `.mvn/toolchains.xml` requesting a JDK with `<vendor>any</vendor>` and `<version>[21,)</version>`.
  - If you have a corporate/global toolchains setup pinning an older JDK, ensure it provides a 21+ JDK or override with this project toolchain.

JVM flags file:
- `.mvn/jvm.config` is intentionally kept free of commented lines to avoid wrapper parsing issues on Windows.
- If you add JVM options, add only valid flags—do not add commented (`#`) lines.

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
- Swagger UI (local): http://localhost:8080/swagger-ui.html
- Swagger UI (deployment): https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002/swagger-ui.html
- OpenAPI JSON (local): http://localhost:8080/v3/api-docs
- OpenAPI JSON (deployment): https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002/v3/api-docs
- Health (Actuator): http://localhost:8080/actuator/health
- Healthz (simple controller): http://localhost:8080/healthz
- Sample API:
  - GET /api/owners
  - GET /api/owners/{id}

OpenAPI server URL:
- The OpenAPI/Swagger "servers" URL is set to:
  https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002
  This ensures Swagger UI "Try it out" calls use https and port 3002, avoiding mixed-content or wrong-host issues.
- You can override it with:
  - JVM property: `-Dpetclinic.openapi.server-url=https://<host>:<port>`
  - Environment variable: `PETCLINIC_OPENAPI_SERVER_URL=https://<host>:<port>`
- If an http URL is provided, the app will force https for the server URL to avoid mixed-content errors.

CORS:
- CORS is configured to allow the deployment origin(s):
  - https://vscode-internal-29701-beta.beta01.cloud.kavia.ai
  - https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002
  with methods GET, POST, PUT, DELETE, OPTIONS and headers Content-Type, Authorization.
- This allows Swagger UI "Try it out" to succeed without CORS errors when accessed from the deployment URL.

Notes:
- Actuator exposes health and info: see src/main/resources/application.properties.
- Health probes (liveness/readiness) are enabled via management.endpoint.health.probes.enabled=true.
- This project is configured for Java 21 using maven-compiler-plugin and Spring Boot 3.2.x.
