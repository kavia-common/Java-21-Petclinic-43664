# Java-21-Petclinic-43664

- Use Maven Wrapper (no global Maven required):
  chmod +x mvnw
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.arguments="--server.port=3002,--server.address=0.0.0.0"

- If port 3002 is busy, choose a different one:
  ./mvnw -q -DskipTests spring-boot:run -Dspring-boot.run.arguments="--server.port=0,--server.address=0.0.0.0"

Notes:
- This project includes .mvn/wrapper with the wrapper jar. If auto-download fails, ensure network access or vendor the jar under .mvn/wrapper/maven-wrapper.jar.
- The app binds to 0.0.0.0 for container preview compatibility.
