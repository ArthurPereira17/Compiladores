#!/bin/bash
set -e
rm -rf build dist/compilador.jar
mkdir -p build dist
javac -encoding UTF-8 --release 17 -Xlint:-deprecation -Xlint:-unchecked -d build src/compilador/*.java
jar cfe dist/compilador.jar compilador.CompilerInterface -C build .
echo "Build OK -> dist/compilador.jar"