#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# Apache Maven Wrapper startup script

set -euf

# Determine the directory of this script
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done
PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR" && pwd`

# Locate Java executable
if [ -n "${JAVA_HOME-}" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVACMD" ] ; then
    echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
    exit 1
  fi
else
  JAVACMD="java"
fi

# Verify Java is available
if ! command -v "$JAVACMD" >/dev/null 2>&1; then
  echo "ERROR: Java executable not found in PATH and JAVA_HOME not set." >&2
  exit 1
fi

WRAPPER_JAR="$BASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$BASEDIR/.mvn/wrapper/maven-wrapper.properties"

# Download wrapper jar if not present
if [ ! -r "$WRAPPER_JAR" ]; then
  if [ -r "$WRAPPER_PROPERTIES" ]; then
    distributionUrl=$(grep -E "^distributionUrl=" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    wrapperUrl=$(grep -E "^wrapperUrl=" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    # create wrapper dir if needed
    mkdir -p "$BASEDIR/.mvn/wrapper"
    if command -v curl >/dev/null 2>&1; then
      curl -fsSL "$wrapperUrl" -o "$WRAPPER_JAR"
    elif command -v wget >/dev/null 2>&1; then
      wget -q "$wrapperUrl" -O "$WRAPPER_JAR"
    else
      echo "ERROR: Couldn't find curl or wget to download Maven Wrapper jar." >&2
      exit 1
    fi
  else
    echo "ERROR: $WRAPPER_PROPERTIES not found." >&2
    exit 1
  fi
fi

# Execute Maven Wrapper
exec "$JAVACMD" \
  -classpath "$WRAPPER_JAR" \
  -Dmaven.multiModuleProjectDirectory="$BASEDIR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
