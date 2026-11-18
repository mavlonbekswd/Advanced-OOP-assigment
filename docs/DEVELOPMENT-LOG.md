# Development log

## 2026-11-03 — Repository preparation
Imported the existing v0.1 starter and unchanged initial Word planning document. Added requirements traceability, roadmap through MVP, proposed UML, report scaffold and testing plan. No complete game or GUI is claimed. GitHub publication was pending at preparation time.

## 2026-11-14 — GitHub import
Imported the prepared starter, architecture, roadmap, test plan and report scaffold into the user-created Advanced-OOP-assigment repository. MVP implementation remains planned. javac is unavailable in this environment, so the Coordinate self-checks were not rerun during this import.

## 2026-11-14 — Ship domain milestone
Added ShipType and Ship, immutable validated positions, distinct hit tracking and sunk state. Chose composition over subclasses because ship types currently differ only by length. Added 52 dependency-free Ship checks. Ran these plus 10 Coordinate checks successfully using the JDK 21 compiler module with source/target 11. The runtime lacks Java 11 release signatures, so this is not verification against the full Java 11 API. Board remains next.

## 2026-11-15 — Board domain milestone
Implemented Board and ShotResult, copied ship ownership, bounds and diagonal separation rules, immutable shot snapshots, setup closure after first shot, duplicate detection and nonempty-fleet victory checks. Ran 10 Coordinate, 52 Ship and 43 Board checks successfully on Java 21. Random fleet generation is next; no playable GUI yet.

## 2026-11-18 — Random fleet placement milestone
Added `battleship.service.RandomShipPlacer`, the first class outside the model
package. It owns the required fleet composition and chooses random positions, while
`Board` stays the only authority on whether a placement is legal. The source of
randomness is injected through a constructor so fleets are reproducible from a seed.
Generation is bounded: 200 position attempts per ship and 100 whole-board attempts,
after which an `IllegalStateException` is raised instead of looping.

Written test first. The checks were written against the public board API before the
class existed, and the compilation failure naming the missing class was observed. A
later check, that a board too small to hold the Battleship fails cleanly, exposed a
real defect: the generator passed a non-positive bound to `Random.nextInt` and
surfaced `IllegalArgumentException: bound must be positive` from inside the JDK. That
failure was observed before the fix.

Added 21 placement checks, giving 126 in total, all passing on OpenJDK 23. Also added
in-code comments naming the OOP concepts used in `Ship`, `Board`, `ShipType` and
`ShotResult`; the assessment asks for these explicitly and only `Coordinate` had them.
No behaviour was changed by those comments. Controller and interface remain unbuilt.
