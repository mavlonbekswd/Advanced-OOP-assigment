# 3. Design Decisions and Justification

*Target: 250 words. Status: complete for the three layers that exist; a short
paragraph on the interface must be added after it is built.*

---

The application is divided into four layers. The model holds the state and the rules,
a service generates a fleet, a controller runs the lifecycle of a game, and the
interface draws it. This follows the Model-View-Controller pattern covered in the
lectures, and it was chosen for a practical reason rather than to demonstrate a
pattern: the rules can be tested completely without ever opening a window.

A decision that had to be made early was whether the three kinds of ship should be
separate subclasses. They were not. The brief distinguishes them only by length, and
a subclass that adds no behaviour its parent does not already have adds complexity
without adding meaning. Instead, a single `Ship` class holds a `ShipType`, which is
an enum recording the required length. This is composition in place of inheritance,
and it can be revisited if the ship types ever need to behave differently.

A second decision concerns where each rule lives. Only `Board` decides whether a
placement is legal. The fleet generator chooses positions at random but never judges
them; it offers each candidate to the board and accepts the verdict. The rule about
ships not touching therefore appears in exactly one method, so it cannot be stated
inconsistently in two places. This follows the DRY principle from the lectures.

A third decision concerns what each class refuses to reveal. `Board` never hands out
its ships, and the controller offers no way to reach the board. The interface
receives the board size, the outcome of every cell already fired at, the player's
counts and whether the game is over. That is enough to draw the grid and announce
victory, and not enough to expose where the ships are hidden.

**PENDING:** a paragraph on the interface design, once it exists.
