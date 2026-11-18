# 4. Implementation

*Target: 300 words of prose, plus the final class model and the differences from the
initial plan. Status: three layers of four are described; the interface section and
the final UML are pending.*

---

## 4.1 The domain model

Four classes hold the state and the rules. `Coordinate` is an immutable pair of
integers with `equals` and `hashCode` overridden so that two coordinates with the
same row and column are treated as the same cell. This matters because coordinates
are used as keys in a map and as members of a set, and without those overrides a
repeated shot would not be recognised.

`Ship` holds its type, the cells it occupies and the set of cells that have been hit.
Its constructor is the only place a ship can be given a shape, and it rejects any
input that is not a straight, unbroken line of the correct length, so a ship object
cannot exist in an invalid state. The occupied cells are copied on the way in and
returned as an unmodifiable list, so outside code cannot alter a placed ship.

`Board` owns the ships and the history of shots. It alone decides whether a placement
is legal, checking that every cell is inside the grid and that no cell of the
candidate lies within one row and one column of any existing ship, which enforces the
diagonal rule described in section 2. `ShotResult` names the four possible outcomes
of a shot: miss, hit, sunk, and already shot.

## 4.2 Random fleet generation

`RandomShipPlacer` builds a complete fleet. It holds the required composition as a
fixed list, longest ship first, because placing the four-cell Battleship while the
board is still empty wastes far fewer attempts than placing it last. For each ship it
chooses an orientation and a starting cell at random, builds a candidate and asks the
board whether that candidate is acceptable.

Retrying is bounded in two places, per ship and per whole board. If a valid fleet
still cannot be produced, the generator raises an exception rather than looping
forever. The source of randomness is supplied through the constructor, which allows a
test to pass a seeded generator and obtain exactly the same fleet every time.

## 4.3 The game controller

`GameController` owns the lifecycle. It starts and restarts games, passes shots to
the board, and counts how many shots the player has taken and how many found a ship.
A repeated shot returns `ALREADY_SHOT` and increases neither count, so clicking the
same square twice cannot inflate a score. Once every ship has been sunk the
controller refuses any further shot, because a finished game is a fact about the
session rather than a rule about the grid.

## 4.4 The user interface

Two classes make up the view. `CellStyle` decides how a single cell should look,
returning a colour and a one-character mark for each possible state. It holds no
components and opens no window, which is what allows the colours the brief requires
to be checked automatically with no display present.

`GameWindow` builds the frame: a status line, a grid of one hundred buttons and a New
Game button. Each cell button is given a listener that reports its own row and column
to the window, which passes them to the controller and then redraws the whole grid
from the controller's state. Cells already fired at are disabled, so a repeated click
is impossible rather than merely ignored, and the whole grid is disabled once the game
has been won.

## 4.5 Object-oriented concepts used

Encapsulation appears throughout: every field is private, collections are copied on
the way in and returned unmodifiable, and `Board` never exposes the ships. Abstraction
appears in `ShipType` and `ShotResult`, which name the few facts the rest of the
program needs without revealing how they are stored. Constructors are used to
guarantee valid state rather than merely to assign fields. Overloading appears in the
two `Board` constructors and the two forms of `createBoard`. Overriding appears in
`equals`, `hashCode` and `toString` on `Coordinate`. The choice of collection was
deliberate in each case: a list for ship positions because order and contiguity
matter, a set for hits because a cell can only be struck once, and a map from cell to
outcome for the shot history because the duplicate rule is a question of whether a
key is already present.

Interfaces appear in the view. Each cell is given an `ActionListener`, which is the
callback interface Swing uses for events: the window supplies the behaviour and the
toolkit decides when to run it. Inheritance appears in `GameWindow`, which extends
`JFrame` in order to be a window while adding the behaviour of this particular one,
and polymorphism appears wherever Swing treats those listeners and components through
their declared types without knowing what this application put behind them.

## 4.6 Final class model

**PENDING — the diagram must still be drawn.** The classes and their relationships
are recorded in `docs/architecture/UML.md`, which now covers all four layers: the
model, the generator, the controller and the view.

## 4.7 Differences from the initial plan

**PARTIALLY COMPLETE.** Three differences have been recorded so far.

The initial plan left open whether the ship types should be subclasses. The finished
code uses one class composed with an enum instead, for the reason given in section 3.

The initial plan expected JUnit to be used. The checks are instead plain Java
harnesses with a `main` method, because the project was built without external
dependencies; this is discussed honestly in section 6.

The initial plan did not say where the game-over guard would live. It was placed in
the controller rather than the board once the distinction between a session fact and
a grid rule became clear during implementation.

The initial plan treated the interface as a single class. It became two: the window
itself and a separate class holding the appearance of a cell. The split was made so
that the colours the brief requires could be tested without opening a window.
