#!/bin/sh
# Maven Wrapper script for Linux

MVNW_DIST_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip"
MVNW_DIST_PATH="$HOME/.m2/wrapper/dists"
MVNW_DIST_ZIP="$MVNW_DIST_PATH/apache-maven-3.9.9-bin.zip"
MVNW_HOME="$MVNW_DIST_PATH/apache-maven-3.9.9"

if [ ! -f "$MVNW_HOME/bin/mvn" ]; then
    echo "Downloading Maven 3.9.9..."
    mkdir -p "$MVNW_DIST_PATH"
    curl -L -o "$MVNW_DIST_ZIP" "$MVNW_DIST_URL" 2>/dev/null || wget -O "$MVNW_DIST_ZIP" "$MVNW_DIST_URL" 2>/dev/null
    if [ $? -eq 0 ] && [ -f "$MVNW_DIST_ZIP" ]; then
        unzip -qo "$MVNW_DIST_ZIP" -d "$MVNW_DIST_PATH"
    else
        echo "Error: Could not download Maven. Please install Maven manually."
        echo "See: https://maven.apache.org/install.html"
        exit 1
    fi
fi

"$MVNW_HOME/bin/mvn" "$@"
