#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Bootstrap Script
# ----------------------------------------------------------------------------
# This script will download and run the Maven Wrapper, enabling builds without
# requiring a globally installed Maven. It downloads the appropriate Maven
# distribution specified in .mvn/wrapper/maven-wrapper.properties.

# Based on Apache Maven Wrapper 3.2.0

WRAPPER_VERSION="3.2.0"

# resolve links - $0 may be a symlink
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")/"$link"
  fi
done
SAVED="$(pwd)"
cd "$(dirname "$PRG")/" >/dev/null
APP_HOME="$(pwd -P)"
cd "$SAVED" >/dev/null

# Begin: extract project base dir
# Resolve the location of the project directory (contains .mvn).
find_maven_basedir() {
  basedir="$1"
  wdir="$basedir"
  while [ "$wdir" != '/' ] ; do
    if [ -d "$wdir/.mvn" ] ; then
      echo "$wdir"
      return
    fi
    # Alternatively we stop at the first directory which contains pom.xml
    if [ -f "$wdir/pom.xml" ] ; then
      echo "$wdir"
      return
    fi
    wdir=$(cd "$wdir/.."; pwd)
  done
  echo "$basedir"
}

BASE_DIR=$(find_maven_basedir "$(pwd)")
MVNW_VERBOSE=${MVNW_VERBOSE:-false}

# Path to the .mvn directory
MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-$BASE_DIR}
export MAVEN_PROJECTBASEDIR

# set -e

# Determine Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    # IBM's JDK on AIX uses strange locations for the executables
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVACMD" ] ; then
    echo "Error: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
    echo "Please set the JAVA_HOME variable in your environment to match the" >&2
    echo "location of your Java installation." >&2
    exit 1
  fi
else
  JAVACMD="java"
  which java >/dev/null 2>&1 || {
    echo "Error: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    echo "Please set the JAVA_HOME variable in your environment to match the" >&2
    echo "location of your Java installation." >&2
    exit 1
  }
fi

# Verify Java version if necessary
# (Wrapper itself supports broad versions; project POM enforces specifics.)

# Download maven-wrapper jar if necessary
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"
if [ "$MVNW_VERBOSE" = true ]; then
  echo "Using base directory: $MAVEN_PROJECTBASEDIR"
fi

# Creates .mvn/wrapper directory if it doesn't exist
mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"

# Read distributionUrl if available
if [ -f "$WRAPPER_PROPERTIES" ]; then
  DISTRIBUTION_URL=$(grep -e '^distributionUrl=' "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
fi

# Default distributionUrl if not set by properties (fallback)
if [ -z "$DISTRIBUTION_URL" ]; then
  DISTRIBUTION_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
fi

# Wrapper jar downloader URL
MVNW_REPOURL=$(grep -e '^wrapperUrl=' "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
if [ -z "$MVNW_REPOURL" ]; then
  MVNW_REPOURL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/$WRAPPER_VERSION/maven-wrapper-$WRAPPER_VERSION.jar"
fi

if [ ! -e "$WRAPPER_JAR" ]; then
  if [ "$MVNW_VERBOSE" = true ]; then
    echo "Downloading Maven Wrapper jar from: $MVNW_REPOURL"
  fi
  jarPath="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
  # Use curl or wget to download
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL -o "$jarPath" "$MVNW_REPOURL"
    ret=$?
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$jarPath" "$MVNW_REPOURL"
    ret=$?
  else
    echo "Error: No 'curl' or 'wget' found to download Maven wrapper." >&2
    exit 1
  fi
  if [ $ret -ne 0 ]; then
    echo "Error: Failed to download Maven wrapper jar from $MVNW_REPOURL" >&2
    exit $ret
  fi
fi

# Provide JVM options via environment or .mvn/jvm.config
# Provide JVM options via environment or .mvn/jvm.config (ignore comments and blank lines)
if [ -z "${MAVEN_OPTS}" ] && [ -f "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config" ]; then
  MAVEN_OPTS="$(grep -v -E '^[[:space:]]*#' "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config" | sed '/^[[:space:]]*$/d' | tr '\n' ' ')"
fi

# Execute Maven WrapperMain
exec "$JAVACMD" \
  $MAVEN_OPTS \
  -classpath "$WRAPPER_JAR" \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
