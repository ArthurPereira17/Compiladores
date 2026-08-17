#!/bin/bash
set -e
rm -rf build dist/compilador.jar
mkdir -p build dist
javac -d build src/compilador/*.java
jar cfe dist/compilador.jar compilador.CompilerInterface -C build .
echo "Build OK -> dist/compilador.jar"