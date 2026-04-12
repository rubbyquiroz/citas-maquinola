@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM ----------------------------------------------------------------------------

@echo off
setlocal enabledelayedexpansion

set "MVNW_REPOURL=https://repo.maven.apache.org/maven2"
set "MVNW_DIST_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip"
set "MVNW_DIST_PATH=%USERPROFILE%\.m2\wrapper\dists"

if not defined M2_HOME set "M2_HOME=%MVNW_DIST_PATH%\apache-maven-3.9.9"

set "MVNW_DIST_BIN=%M2_HOME%\bin"

if exist "%MVNW_DIST_BIN%\mvn.cmd" (
  "%MVNW_DIST_BIN%\mvn.cmd" %*
  exit /b !errorlevel!
)

echo Downloading Maven 3.9.9...
if not exist "%MVNW_DIST_PATH%" mkdir "%MVNW_DIST_PATH%"

powershell -Command "(New-Object Net.WebClient).DownloadFile('%MVNW_DIST_URL%', '%MVNW_DIST_PATH%\apache-maven-3.9.9-bin.zip')"

powershell -Command "Expand-Archive -Path '%MVNW_DIST_PATH%\apache-maven-3.9.9-bin.zip' -DestinationPath '%MVNW_DIST_PATH%' -Force"

"%MVNW_DIST_BIN%\mvn.cmd" %*
