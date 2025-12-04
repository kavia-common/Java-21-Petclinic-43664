#!/bin/sh
# Simple fallback script to build (using Maven or Maven Wrapper) and run the Spring Boot app via java -jar.
# Usage examples:
#   ./run.sh                # build (if needed) and run on default port 8080
#   ./run.sh -Dserver.port=3002 -Dserver.address=0.0.0.0
# Any extra arguments are passed to 'java -jar' as JVM system properties.

set -eu

PROJECT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
cd "$PROJECT_DIR"

JAR_GLOB="target/*-SNAPSHOT.jar"
FINAL_JAR=""

log() {
  printf '%s\n' "$*" 1>&2
}

ensure_java() {
  if command -v java >/dev/null 2>&1; then
    return 0
  fi
  log "Error: 'java' command not found in PATH. Please install Java 21 and try again."
  exit 1
}

build_with_maven() {
  if [ -x "./mvnw" ]; then
    if ./mvnw -v >/dev/null 2>&1; then
      log "Using ./mvnw to build..."
      ./mvnw -q -DskipTests package
      return $?
    else
      log "./mvnw failed. Will try system Maven if available..."
    fi
  fi
  if command -v mvn >/dev/null 2>&1; then
    log "Using system 'mvn' to build..."
    mvn -q -DskipTests package
    return $?
  fi
  log "No working Maven (wrapper or system) found. If a jar already exists, we will attempt to run it."
  return 1
}

find_jar() {
  # Prefer non-SNAPSHOT final jar if present, else any jar in target
  if [ -d target ]; then
    # Try to pick the Boot repackage jar (normally artifact-version.jar)
    FINAL_JAR="$(ls -1 target/*.jar 2>/dev/null | head -n 1 || true)"
  else
    FINAL_JAR=""
  fi
}

ensure_java

# Try to build. If build fails, continue in case a jar already exists.
build_with_maven || true

find_jar

if [ ! -f "$FINAL_JAR" ]; then
  log "Error: Could not find a built jar in target/. Please ensure a successful build first."
  exit 1
fi

log "Running: java $* -jar \"$FINAL_JAR\""
exec java $* -jar "$FINAL_JAR"
