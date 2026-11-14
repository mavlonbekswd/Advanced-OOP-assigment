# Initial architecture checkpoint — 03 November 2026

## Source-informed decisions

- **Required** (official Part A): 10 × 10 board; Battleship ×1 of length 4, Cruiser ×2 of length 3, Destroyer ×3 of length 2; no overlap or touching; red hit, blue miss; end when all ships are sunk.
- **Selected target** (official enhancement examples and Week 4): random placement, mouse-controlled GUI.
- **Proposed design** (Week 6): MVC-inspired division between Model, View and Controller.
- **Proposed collections** (Week 7): use a 2D array or fixed-size board representation for cells, and a `List<Ship>` for six ships. Choose based on operations rather than just to demonstrate collections.
- **Proposed GUI** (Week 8): Java Swing, `JFrame`, `JPanel`, `GridLayout`; handle clicks using listeners (Week 9).
- **Not required**: database, file save/load, multithreading.

## Responsibility map

| Layer | Planned class | Responsibility |
| --- | --- | --- |
| Model | `Coordinate` | An immutable row/column location (implemented). |
| Model | `Ship` | A ship's occupied locations, hits and sunk state (implemented). |
| Model | `Board` | Board size, ship placement validation, shot state and shot results. |
| Service | `RandomShipPlacer` | Place an entire valid fleet using random orientation/start coordinates. |
| Controller | `GameController` | Start/restart game, process shots, expose results for the UI. |
| View | `GameWindow` | Render Swing grid and actions; no placement rules. |

## Architectural trade-offs to record

1. Keep the UI separate from the domain model so rules can be tested without launching a window.
2. `Ship` inheritance is not decided: three kinds of ship differ by length in the brief. Using subclasses purely to demonstrate inheritance risks unnecessary complexity. Re-evaluate after `Ship` exists; the lecturer's Week 3 Factory hint is not a mandate.
3. The official wording “ships do not touch” is currently interpreted as including diagonals. Record and test this assumption; ask the lecturer if needed.
4. `Coordinate` accepts any integer pair because the board, not the value object, controls boundary policy. This makes out-of-range tests straightforward.
5. `Coordinate` is immutable, so it has getters but no setters. Encapsulation does not mean every field must have a setter.
6. The initial design document remains a pre-code snapshot. Record implementation-time changes separately and truthfully.

## First accepted working milestone

- `Coordinate(3,5)` correctly stores two integers.
- `isWithin(10)` accepts positions 0–9 and rejects out-of-bounds coordinates.
- Equal coordinates compare equal and have matching hashes.
- Tests can run without any GUI or external libraries.

## Report evidence

The starter has 10 checks executed in a standalone harness (not JUnit). Preserve a terminal screenshot for development evidence if useful. After `Ship` and `Board` exist, add substantive unit tests, a functional test table and screenshots matching actual behaviour.

## 14 November 2026 — Ship implementation decision
`Ship` is a final concrete class composed with `ShipType`, an enum encoding lengths 4/3/2. Types currently differ only by length, so subclasses would add no distinct behaviour. Positions are defensively copied, sorted and exposed as an immutable list; hit state is a private Set. Construction rejects duplicate, gapped, diagonal and bent shapes. Board bounds and inter-ship adjacency remain responsibilities of the future Board. `registerHit` returns true only for a newly recorded hit; Board will distinguish a miss from a duplicate shot.
