# Board test evidence — 15 November 2026

Ran the compiler and all three self-test main classes on OpenJDK 21.0.13:

```bash
java -m jdk.compiler/com.sun.tools.javac.Main -source 11 -target 11 -Xlint:all -d out src/main/java/battleship/model/*.java src/main/java/battleship/App.java src/test/java/battleship/model/*.java
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
```

PASS: 10 Coordinate self-checks
PASS: 52 Ship self-checks
PASS: 43 Board self-checks

Board coverage: default/custom sizes; empty-board rules; invalid/null inputs; all four boundaries; failed placement atomicity; overlap; horizontal, vertical and diagonal contact; valid separation; edge placement; defensive ship copying; immutable independent shot snapshots; failed shot atomicity; setup closure; miss/hit/sunk/duplicate outcomes; unique shot count; multiple-ship victory.

Plain Java harnesses, not JUnit. Source/target 11 compilation emits a system modules path warning; this environment lacks --release 11 support. Actual execution is verified on Java 21 only. Fleet composition enforcement, random placement, controller and GUI tests belong to later milestones.
