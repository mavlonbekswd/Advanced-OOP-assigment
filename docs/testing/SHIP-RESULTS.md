# Ship milestone test evidence — 14 November 2026

Executed on OpenJDK 21.0.13:

```bash
java -m jdk.compiler/com.sun.tools.javac.Main -source 11 -target 11 -Xlint:all -d out src/main/java/battleship/model/*.java src/main/java/battleship/App.java src/test/java/battleship/model/*.java
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
```

Results: PASS: 10 Coordinate self-checks; PASS: 52 Ship self-checks.

Coverage: all three ship types, defensive copying, immutable getters, misses, repeat hits by coordinate value, final-hit sinking, unordered vertical placement, invalid lengths, duplicate cells, diagonals, bent and gapped shapes, null inputs.

These are plain Java test harnesses, not JUnit. Compilation emitted one system modules path warning for source/target 11. The trimmed runtime does not support --release 11, so execution was verified on Java 21 only. No Board or GUI claims are made.
