# Maven Wrapper Notes

- Use `./mvnw` (Unix/macOS) or `.\\mvnw.cmd` (Windows) for all Maven commands.
- Ensure `mvnw` has:
  - Shebang: `#!/bin/sh`
  - LF line endings (use `dos2unix mvnw` if needed)
  - Executable bit: `chmod +x mvnw`
- Wrapper configuration:
  - `.mvn/wrapper/maven-wrapper.properties` controls:
    - `distributionUrl` (Maven 3.9.x binary)
    - `wrapperUrl` (maven-wrapper jar 3.2.0)
- Quick verification:
  - `./mvnw -v` should print Maven/Java info (confirms no exit code 127).
