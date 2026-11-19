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

## 15 November 2026 — Board implementation
Board owns private copies of placed ships and a Map of shot coordinates to original outcomes. A default constructor creates a 10×10 board; a size overload supports small test boards. Placement rejects bounds violations, overlap and all eight neighbouring directions. Failed placement and invalid shots leave state unchanged. Setup closes after the first valid shot. Empty boards cannot be fired on or count as victory. ShotResult distinguishes MISS, HIT, SUNK and ALREADY_SHOT. Read access exposes an immutable snapshot of shot history, never mutable ships or their hidden positions. Exact fleet composition remains the setup service responsibility. The future controller must stop accepting new shots after victory; Board retains domain-level shot processing.

## 18 November 2026 — Random fleet placement
`RandomShipPlacer` lives in a new `battleship.service` package, matching the layer
split planned above. It holds the fleet the brief requires as a fixed list, longest
ship first, because placing the four-cell Battleship while the board is empty wastes
far fewer attempts than placing it last.

The service asks `Board.canPlaceShip` rather than re-checking bounds, overlap or
adjacency. Those rules stay stated once, which is the DRY principle from Week 7, and
it means a change to the separation rule cannot leave the generator disagreeing with
the board.

`Random` is a constructor parameter. An unseeded default is offered for the game, and
a seeded instance makes a fleet reproducible in a test. This is dependency injection
used for a concrete reason, not to demonstrate a pattern: without it the only way to
test the generator would be to add a test-only accessor to production code.

Retrying is bounded in two places, per ship and per whole board, and exhaustion raises
`IllegalStateException`. A random generator that can fail must say so rather than
block the application. `createBoard(int)` overloads the standard call so this failure
path can be exercised on a board where the fleet genuinely cannot fit.

Fleet composition now has an owner. `Board` still deliberately does not police it,
because a board is also useful in tests with one or two ships.

## 18 November 2026 — Game controller
`GameController` lives in `battleship.controller` and owns the lifecycle of a game:
starting one, restarting, taking a shot and deciding when the game is won. It holds
no rules about placement or hits, which stay in `Board`, and it draws nothing.

What the controller adds over the board is the player's own progress. The board
records what happened on each cell; the controller counts how many shots the player
has taken and how many found a ship. The duplicate-shot rule matters here: a repeated
click returns `ALREADY_SHOT` and increments neither count, so a player cannot inflate
their score, and the interface can tell the difference between a wasted click and a
real miss.

The controller closes the game after victory. The board would happily keep accepting
shots at a destroyed fleet, because "the game is finished" is a lifecycle fact rather
than a board rule. Putting the guard here keeps that distinction clean and means the
interface cannot accidentally allow a shot after the victory message appears.

Deliberately, `getBoard()` does not exist. The interface is given the board size, the
outcome of each cell already fired at, the two counts and whether the game is over.
That is everything needed to draw the grid and nothing that would let a careless view
reveal where the ships are.

The generator is injected for the same reason it is injected into the placer: a
seeded game is reproducible, so a test can assert an exact hit count of 16 instead of
a range, with no test-only accessor added to production code.

## 18 November 2026 — Swing interface
The view is two classes rather than one. `CellStyle` answers a single question, what
one cell should look like given what the player knows about it, and returns a colour
and a one-character mark. It touches no Swing component and opens no window, so the
colours the brief requires can be checked automatically with no display present.
`GameWindow` builds the frame and wires each of the hundred cells to the controller.

The window holds a controller and nothing else. There is no path from the interface
to a board, so the view cannot read hidden ship positions even by accident. On every
click it asks the controller to take the shot and then redraws every cell from the
controller's reported state, rather than trying to update one cell in place. Redrawing
everything is slightly wasteful and much harder to get wrong.

Cells already fired at are disabled rather than merely ignored, and the whole grid is
disabled once the game is won, so the controller's rules are reflected in what the
player can physically click.

The cross-platform look and feel is requested at start-up. Some native look and feels
ignore a button's background colour, and the brief requires blue and red cells, so
this is a requirement rather than a preference. Two rendering faults were found only
by looking at the running application: a font with no glyph for the chosen symbols,
and a default button margin that clipped a single character to an ellipsis. Both are
recorded in testing/GUI-RESULTS.md.

## 19 November 2026 — difficulty, the fleet report, and a three-part view
The view is now four classes. `CellStyle` is unchanged. `BoardPanel` owns the grid
and the row and column labels, and reports a click through `ShotListener`, an
interface it declares itself; it knows nothing about what a click means, which is
what keeps the rules out of the drawing code. `FleetPanel` lists the enemy ships and
the running counts. `GameWindow` assembles the two panels, holds the toolbar and the
status line, and still holds a controller and nothing else.

`Difficulty` is an enum in the model pairing a board size with a shot limit. The size
goes to `RandomShipPlacer.createBoard(size)`, which already existed and was already
tested, so the setting cost no change to the placement rules at all. The shot limit is
held and enforced by `GameController`, because the number of shots a player is allowed
is a fact about one session of play, while `Board` knows only squares, ships and what
has been fired at. `isWon()`, `isLost()` and `isGameOver()` make the three outcomes
explicit; a shot at a finished game is refused rather than ignored.

The fleet panel raised the question the encapsulation rule was always going to face:
how does a view name the ships that have gone down without learning where any ship is?
`Board.getFleetStatus()` answers it by reducing each ship to a `ShipStatus`, an
immutable pair of type and condition with no positions in it, returned as an
unmodifiable list. The ships themselves still never leave the board.

Easy allows one shot per square deliberately. It cannot be lost, which makes it the
only setting on which a check that fires at every square is guaranteed to finish in a
win; the sweep-based controller checks were moved onto it, and defeat is checked
separately by playing out ten seeded games on Hard.

## 19 November 2026, later — asking the font instead of assuming
`CellStyle` gained `Marks`, an enum of two sets of cell marks, and `marksFor(Font)`,
which chooses between them by asking the font whether it has a glyph for each symbol.
`labelFor` takes the chosen set as an argument rather than looking a font up itself,
so the class keeps no dependency on Swing beyond `java.awt.Font` and both branches
can be checked with no display. `BoardPanel` asks once per redraw and passes the
answer to every square.

This closes the loop on the first rendering defect. The original fix was to stop
using symbols; this one uses them when they will work and falls back when they will
not, which is the difference between avoiding a failure and handling it.

## 19 November 2026, evening — the mark colour
`CellStyle` also answers what colour a mark is drawn in, so every appearance decision
stays in the one class. One colour serves all three states: a second would have to be
read against the first to carry any meaning, and the mark's whole purpose is to be
readable without comparing colours.

Applying it needed a look and feel default rather than a property of the button,
because `setForeground` is ignored on a disabled button and a square is disabled as
soon as it has been fired at. `BoardPanel` sets `Button.disabledText`, since it is
the class that disables squares. That is a global of the look and feel, which is
worth naming plainly: it is idempotent, and no other button in the window is ever
disabled.
