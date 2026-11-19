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

## 2026-11-19 verification, evening

316 checks ran from a clean build after the marks were given their own colour: three
new checks measure the contrast between that colour and each of the three cell
colours, and require at least the 3 to 1 ratio the accessibility guidelines ask of
large text. Measured with white: 3.68 on a miss, 4.23 on a hit, 9.01 on a sunk
square. The grey it replaces, the Metal look and feel's #999999, gives 1.29, 1.48 and
3.16, so two of the three failed.

## 2026-11-19 verification, later

313 checks ran from a clean build after the cell marks were changed to drawn symbols
with an ASCII fallback: the CellStyle harness went from 12 checks to 29, covering both
sets of marks, the choice between them and the fallback taken when no font is given.
Every other harness is unchanged and still passes.

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
