# Random fleet placement test evidence — 18 November 2026

Compiled and ran every self-test main class from a clean `out/` directory:

```bash
rm -rf out && mkdir -p out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
java -cp out battleship.service.RandomShipPlacerSelfTest
```

```
PASS: 10 Coordinate self-checks
PASS: 52 Ship self-checks
PASS: 43 Board self-checks
PASS: 21 RandomShipPlacer self-checks
```

126 checks in total. Compilation produced no errors and no `-Xlint:all` warnings.

## Environment

OpenJDK 23.0.2 on macOS. This differs from the Java 21 runs recorded on 14 and 15
November and from the `-source 11 -target 11` setting used then: this run was
compiled and executed with the default release of the installed JDK, so it is not a
verification against the Java 11 API.

## How the placer is checked

The generator is inspected only through the public board API. No test-only accessor
was added to `Board`, so the production class still never exposes hidden ship
positions. A test board is filled, every one of its 100 cells is fired at, and the
cells that answered `HIT` or `SUNK` are collected. Those cells are then grouped by
eight-way adjacency.

Because the separation rule forbids ships from touching in any direction, each
connected group must be exactly one ship. The expected group count and group sizes
therefore prove fleet composition, absence of overlap and the separation rule at the
same time.

## Coverage

- Standard board size and a ship count of six.
- Sixteen occupied cells in total, and every ship sinking once all cells are struck.
- Six separated groups with sizes 4, 3, 3, 2, 2, 2.
- Every group a straight, unbroken horizontal or vertical line.
- A seeded `Random` reproducing an identical fleet; a different seed producing a
  different fleet.
- 1,000 consecutive fleets generated from seeds 0–999, every one of them valid
  against all of the rules above, in roughly 0.16 s.
- Both horizontal and vertical ships occurring across those 1,000 fleets.
- A null `Random` rejected.
- A 4x4 board, on which the fleet cannot be separated, reported as
  `IllegalStateException` rather than looped on.
- A 3x3 board, too small even for the Battleship, reported the same way. The first
  version of the generator failed this check with a confusing
  `IllegalArgumentException: bound must be positive` raised from inside
  `java.util.Random`; the check was written first, the failure was observed, and the
  generator now refuses a ship longer than the board before choosing a position.
- A board size of zero still rejected by the `Board` constructor.

## Limitations

These remain plain Java harnesses, not JUnit. They cover the model and the placement
service only. No controller, no interface and no functional testing of a running game
exist yet, so no claim is made about playable behaviour.
