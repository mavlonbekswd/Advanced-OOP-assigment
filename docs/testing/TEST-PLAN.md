# Testing plan and evidence

| Area | Planned checks | Current result |
|---|---|---|
| Coordinate | getters, boundary policy, equality/hash, string representation | 10 checks passing |
| Ship | hit tracking, repeated hit, sunk only at full length | 52 checks passing; see SHIP-RESULTS.md |
| Board | invalid placement, bounds, overlap, all eight touching neighbours, hit/miss, repeat shot | 43 checks passing; see BOARD-RESULTS.md |
| Random placement | seeded repeatable fleet checks, exact counts and lengths, retry exhaustion | 21 checks passing; see PLACER-RESULTS.md |
| Controller | victory, post-game input, restart | 32 checks passing; see CONTROLLER-RESULTS.md |
| GUI | mouse input, colours, messages, restart on real display | 12 CellStyle checks plus 9 functional cases; see GUI-RESULTS.md |

Add actual run date, command, outcome and environment. Automated model checks do not prove GUI behaviour. Add JUnit coverage before claiming completion of the unit-testing milestone.

## 2026-11-19 verification

296 checks ran from a clean build on OpenJDK 23.0.2: 10 Coordinate, 52 Ship, 55 Board,
26 Difficulty, 21 RandomShipPlacer, 120 GameController, 12 CellStyle. No compiler
warnings under `-Xlint:all` and no `javadoc` warnings. The table above is the plan as
it stood before the difficulty setting was added; the rows it lists are all still
covered, and three areas grew:

| Area | What was added | Checks |
|---|---|---|
| Difficulty | three settings, their sizes and limits, and the relations between them | 26 new |
| Board | the fleet report: contents, order, and that a snapshot cannot be changed | 43 to 55 |
| Controller | the chosen setting, the shot limit, shots remaining, the fleet report, and ten seeded games played out on Hard to prove a game can be lost | 32 to 120 |

The interface was tested against fifteen functional cases, five of them new; see
GUI-RESULTS.md.

## 2026-11-18 verification
170 checks ran from a clean build on OpenJDK 23: 10 Coordinate, 52 Ship, 43 Board,
21 RandomShipPlacer, 32 GameController, 12 CellStyle. No compiler warnings and no
javadoc warnings. The interface was additionally tested by hand against nine
functional cases on the running application; see GUI-RESULTS.md.

## 2026-11-03 setup verification
Compilation attempted in this environment, but `javac` is not installed. Tests were not executed here. Earlier reported results are not a fresh verification.
