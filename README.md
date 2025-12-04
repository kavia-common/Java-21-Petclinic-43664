# Java-21-Petclinic

This project now includes the Maven Wrapper so you can build and run it without having Maven installed globally.

How to run (development):
- Unix/macOS:
  ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"
- Windows (PowerShell or CMD):
  .\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=3002 -Dserver.address=0.0.0.0"

Notes:
- The wrapper will download Maven (3.9.6) on first run into your local cache.
- Requires Java 21 (ensure JAVA_HOME points to a Java 21 installation or that 'java' in PATH is Java 21).
- No application code was changed; only build/run tooling was added.
