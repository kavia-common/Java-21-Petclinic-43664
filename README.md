# Java-21-PetClinic-43664

- Use Maven Wrapper (no global Maven required):
  chmod +x mvnw
  # Preferred: pass JVM args (avoids argument parsing issues)
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"

- If port 3002 is busy, use a random free port:
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=0 -Dserver.address=0.0.0.0"

- Alternative (works in most environments but may parse commas incorrectly):
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.arguments="--server.port=3002 --server.address=0.0.0.0"

Build notes (Java 17/21):
- The build defaults to Java 17 in pom.xml so it can run where only JDK 17 is available.
- To build targeting Java 21 when JDK 21 is present, run:
  ./mvnw -DskipTests -Dcompile.release=21 -Djava.version=21 -Dmaven.compiler.source=21 -Dmaven.compiler.target=21 clean package

Runtime and preview:
- The preview expects the app to listen on port 3002. If you see "Port 3002 was already in use", either stop the conflicting process, or start with a random free port:
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=0 -Dserver.address=0.0.0.0"
  Then check the logs for "Tomcat started on port <NNNNN>" and open that port.
- Root endpoint "/" is provided and returns a JSON message pointing to useful endpoints.
- API docs:
  - Swagger UI: http://localhost:3002/swagger-ui/index.html
  - OpenAPI JSON: http://localhost:3002/v3/api-docs
  - Preview base: https://vscode-internal-18737-beta.beta01.cloud.kavia.ai:3002/
    - Swagger UI: https://vscode-internal-18737-beta.beta01.cloud.kavia.ai:3002/swagger-ui/index.html
    - OpenAPI JSON: https://vscode-internal-18737-beta.beta01.cloud.kavia.ai:3002/v3/api-docs

Notes:
- This project includes .mvn/wrapper with the wrapper jar. If auto-download fails, ensure network access or vendor the jar under .mvn/wrapper/maven-wrapper.jar.
- The app binds to 0.0.0.0 for container preview compatibility.
- If a port is in use, prefer server.port=0 which chooses a free port automatically. Check logs for the selected port (e.g., "Tomcat started on port NNNNN").

Note: Minor README capitalization update.
