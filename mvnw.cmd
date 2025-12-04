@ECHO OFF
REM ----------------------------------------------------------------------------
REM Maven Wrapper Bootstrap Script for Windows
REM ----------------------------------------------------------------------------
REM This script will download and run the Maven Wrapper, enabling builds without
REM requiring a globally installed Maven. It downloads the Maven distribution
REM specified in .mvn/wrapper/maven-wrapper.properties.

SETLOCAL

SET WRAPPER_VERSION=3.2.0

SET "CURRENT_DIR=%~dp0"
IF NOT DEFINED MAVEN_PROJECTBASEDIR (
  SET "MAVEN_PROJECTBASEDIR=%CURRENT_DIR%"
)

REM Find project base dir (with .mvn or pom.xml)
:findBaseDir
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn" GOTO endFindBaseDir
IF EXIST "%MAVEN_PROJECTBASEDIR%\pom.xml" GOTO endFindBaseDir
CD ..
IF "%CD%"=="%MAVEN_PROJECTBASEDIR%" GOTO endFindBaseDir
SET "MAVEN_PROJECTBASEDIR=%CD%"
GOTO findBaseDir
:endFindBaseDir

SET "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET "WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"

IF EXIST "%WRAPPER_PROPERTIES%" (
  FOR /F "usebackq tokens=1,* delims==" %%A IN ("%WRAPPER_PROPERTIES%") DO (
    IF /I "%%A"=="distributionUrl" SET "DISTRIBUTION_URL=%%B"
    IF /I "%%A"=="wrapperUrl" SET "MVNW_REPOURL=%%B"
  )
)

IF NOT DEFINED DISTRIBUTION_URL (
  SET "DISTRIBUTION_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
)

IF NOT DEFINED MVNW_REPOURL (
  SET "MVNW_REPOURL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar"
)

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper jar from: %MVNW_REPOURL%
  FOR %%I IN (curl.exe) DO SET "HAS_CURL=%%~$PATH:I"
  FOR %%I IN (wget.exe) DO SET "HAS_WGET=%%~$PATH:I"
  IF DEFINED HAS_CURL (
    curl -fsSL -o "%WRAPPER_JAR%" "%MVNW_REPOURL%"
    IF ERRORLEVEL 1 GOTO errorDownload
  ) ELSE IF DEFINED HAS_WGET (
    wget -q -O "%WRAPPER_JAR%" "%MVNW_REPOURL%"
    IF ERRORLEVEL 1 GOTO errorDownload
  ) ELSE (
    ECHO Error: Neither curl nor wget found to download Maven wrapper.
    EXIT /B 1
  )
)

SET "JAVACMD=java"
IF DEFINED JAVA_HOME (
  IF EXIST "%JAVA_HOME%\bin\java.exe" SET "JAVACMD=%JAVA_HOME%\bin\java.exe"
)

REM Build MAVEN_OPTS from .mvn\jvm.config if present
SET MAVEN_OPTS=%MAVEN_OPTS%
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config" (
  FOR /F "usebackq delims=" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config") DO (
    SET MAVEN_OPTS=!MAVEN_OPTS! %%A
  )
)

"%JAVACMD%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
EXIT /B %ERRORLEVEL%

:errorDownload
ECHO Error: Failed to download Maven wrapper jar from %MVNW_REPOURL%
EXIT /B 1
