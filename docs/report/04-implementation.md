# 4. Implementation

*Target: 300 words plus the final class model. Status: complete except the diagram.*

---

## 4.1 How the application is built

Nine classes sit in the four layers described in section 3.

In the model, `Coordinate` is an immutable cell location overriding `equals` and
`hashCode`, without which a repeated shot would not be recognised. `Ship` holds its
type, its cells and the cells hit so far, and its constructor rejects anything that
is not a straight unbroken line of the right length, so a ship cannot exist in an
invalid state. `Board` owns the ships and the shot history and is the sole authority
on placement, rejecting any cell out of bounds or within one row and one column of an
existing ship, which is how the diagonal rule is enforced.

`RandomShipPlacer` builds a fleet, longest ship first, since placing the four-cell
Battleship on an empty board wastes far fewer attempts. Retrying is bounded per ship
and per board, and exhaustion raises an exception rather than looping.
`GameController` starts and restarts games, passes shots to the board and counts
shots and hits separately, and refuses further shots once the fleet is destroyed.

In the interface, `CellStyle` returns the colour and mark for one cell and touches no
component, which lets the required colours be checked with no display. `GameWindow`
gives each cell a listener reporting its own position and redraws the grid from the
controller after every shot.

## 4.2 Object-oriented concepts

**Encapsulation:** every field private, collections copied in and returned
unmodifiable, ships never exposed. **Abstraction:** `ShipType` and `ShotResult` name
what the program needs without revealing storage. **Inheritance:** `GameWindow`
extends `JFrame`. **Polymorphism:** Swing handles listeners and components through
their declared types. **Interfaces:** each cell has an `ActionListener`. **State:** a
ship's hits and the shot history. **Constructors:** used to guarantee validity.
**Overloading:** two `Board` constructors. **Overriding:** `equals`, `hashCode`,
`toString`. **Architectural pattern:** MVC. **Collections:** a list for positions
because order matters, a set for hits because a cell is struck once, a map for
history because the duplicate rule is key presence.

Abstract classes, a designed pattern and mutator methods are absent; section 7.4
explains each.

## 4.3 Final class model

**PENDING — diagram to be drawn,** covering all four layers.

## 4.4 Differences from the initial plan

Four changes were made. The plan left open whether ship types should be subclasses;
one class composed with an enum was used instead, for the reason in section 3. The
plan expected JUnit; plain Java harnesses were used, leaving no dependencies, at the
cost given in section 6.2. The plan did not say where the game-over guard would live;
it went to the controller once the difference between a session fact and a grid rule
became clear. Finally, the plan treated the interface as one class, and it became
two, so the required colours could be tested without opening a window.
