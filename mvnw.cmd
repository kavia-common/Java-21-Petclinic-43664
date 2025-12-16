@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one or more
@REM contributor license agreements.  See the NOTICE file distributed with
@REM this work for additional information regarding copyright ownership.
@REM The ASF licenses this file to You under the Apache License, Version 2.0
@REM (the "License"); you may not use this file except in compliance with
@REM the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------

@echo off
setlocal

set BASEDIR=%~dp0

if not defined JAVA_HOME goto findJavaFromPath

set JAVACMD=%JAVA_HOME%\bin\java.exe
if exist "%JAVACMD%" goto execute

echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
goto error

:findJavaFromPath
set JAVACMD=java.exe
where %JAVACMD% >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo ERROR: Java executable not found in PATH and JAVA_HOME not set. 1>&2
goto error

:execute
set WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=%BASEDIR%\.mvn\wrapper\maven-wrapper.properties

if not exist "%WRAPPER_JAR%" (
  if not exist "%WRAPPER_PROPERTIES%" (
    echo ERROR: %WRAPPER_PROPERTIES% not found. 1>&2
    goto error
  )
  for /f "tokens=1,* delims==" %%A in ('findstr /r "^wrapperUrl=" "%WRAPPER_PROPERTIES%"') do set WRAPPER_URL=%%B
  if exist "%WRAPPER_JAR%" goto run
  if exist "%BASEDIR%\.mvn\wrapper" goto dl
  mkdir "%BASEDIR%\.mvn\wrapper"
:dl
  powershell -Command "try { Invoke-WebRequest -UseBasicParsing -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%' } catch { $ProgressPreference = 'SilentlyContinue'; (New-Object System.Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%') }"
)

:run
"%JAVACMD%" -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
exit /b %ERRORLEVEL%

:error
endlocal
exit /b 1
