# 3. Design Decisions and Justification

*Target: 250 words. Status: complete.*

---

The application is divided into four layers: a model holding the state and the
rules, a service generating a fleet, a controller running the lifecycle of a game,
and an interface drawing it. This follows the Model-View-Controller pattern covered
in the lectures, and was chosen for a practical reason rather than to demonstrate a
pattern: the rules can be tested completely without opening a window.

The first decision was whether the three kinds of ship should be subclasses. They
were not: the brief distinguishes them only by length, and a subclass adding no
behaviour its parent lacks adds complexity without meaning. A single `Ship` class
holds a `ShipType` enum recording the length instead. The Week 3 lecture reaches the
same conclusion about this game, noting a ship hierarchy is not really necessary for
an example this simple though it would help in a more complex one.

The second concerns where each rule lives. Only `Board` decides whether a placement
is legal. The fleet generator chooses positions at random but never judges them; it
offers each candidate to the board and accepts the verdict. The rule about ships not
touching therefore appears in exactly one method and cannot be stated inconsistently
in two places, which is the DRY principle from the lectures.

The third concerns what each class refuses to reveal. `Board` never hands out its
ships and the controller offers no route to the board, so the interface receives the
board size, the outcome of every cell already fired at, the counts and whether the
game is over: enough to draw the grid and announce victory, not enough to expose
where the ships are.

Finally, the cross-platform look and feel is requested at start-up, because some
native ones ignore a button's background colour and the brief requires blue and red
cells. That makes it a requirement rather than a matter of taste.
