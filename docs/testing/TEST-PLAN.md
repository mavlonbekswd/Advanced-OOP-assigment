# Testing plan and evidence

| Area | Planned checks | Current result |
|---|---|---|
| Coordinate | getters, boundary policy, equality/hash, string representation | 10 checks passing |
| Ship | hit tracking, repeated hit, sunk only at full length | 52 checks passing; see SHIP-RESULTS.md |
| Board | invalid placement, bounds, overlap, all eight touching neighbours, hit/miss, repeat shot | 43 checks passing; see BOARD-RESULTS.md |
| Random placement | seeded repeatable fleet checks, exact counts and lengths, retry exhaustion | 21 checks passing; see PLACER-RESULTS.md |
| Controller | victory, post-game input, restart | 32 checks passing; see CONTROLLER-RESULTS.md |
| GUI | mouse input, colours, messages, restart on real display | Not implemented |

Add actual run date, command, outcome and environment. Automated model checks do not prove GUI behaviour. Add JUnit coverage before claiming completion of the unit-testing milestone.

## 2026-11-18 verification
158 checks ran from a clean build on OpenJDK 23: 10 Coordinate, 52 Ship, 43 Board,
21 RandomShipPlacer, 32 GameController. No compiler warnings and no javadoc warnings.
The GUI row above remains untested because no interface exists.

## 2026-11-03 setup verification
Compilation attempted in this environment, but `javac` is not installed. Tests were not executed here. Earlier reported results are not a fresh verification.
