#!/bin/bash

# Directories containing the JAR files (space-separated)
jar_directories=("lib/spec" "lib/" "target/")

# Initialize an empty classpath
classpath=""

# Loop through specified directories and append JAR files to the classpath
for dir in "${jar_directories[@]}"; do
    for jar_file in "$dir"/*.jar; do
        if [ -f "$jar_file" ]; then
            if [ -z "$classpath" ]; then
                classpath="$jar_file"
            else
                classpath="$classpath:$jar_file"
            fi
        fi
    done
done

# Print the constructed classpath
echo "$classpath"

java -cp $classpath ui.Main