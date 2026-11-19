#!/bin/bash
# Compiles and runs the throwaway design prototype.
#
# Nothing under src/ or docs/ is touched by this script. The existing game is
# still started with:  java -cp build battleship.App
set -e
cd "$(dirname "$0")/.."
OUT=prototype/build
rm -rf "$OUT"
mkdir -p "$OUT"
javac -d "$OUT" $(find src/main/java -name '*.java')
javac -cp "$OUT" -d "$OUT" prototype/BattleshipPrototype.java
echo "Starting the prototype. Close the window to stop it."
java -cp "$OUT" BattleshipPrototype "$@"
