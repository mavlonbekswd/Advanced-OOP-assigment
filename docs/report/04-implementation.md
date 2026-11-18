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

**PENDING — not yet implemented.**

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

**PENDING:** interfaces, inheritance and polymorphism are not yet demonstrated. They
are expected to arise naturally in the interface layer through event listeners.

## 4.6 Final class model

**PENDING — diagram must be drawn once the interface exists.**

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
