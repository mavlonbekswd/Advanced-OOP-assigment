# 6. Testing

*Target: 400 words. Status: complete.*

---

## 6.1 Unit testing

Testing ran alongside development: each class was checked before the next was
written, and each check was watched failing before the code that satisfies it was
written, because a check that has never failed may be testing the wrong thing. The
checks are plain Java classes with a `main` method rather than JUnit tests, which
leaves the project free of dependencies at the cost of not being a recognised
framework.

One hundred and seventy checks pass: ten for `Coordinate`, fifty-two for `Ship`,
forty-three for `Board`, twenty-one for `RandomShipPlacer`, thirty-two for
`GameController` and twelve for `CellStyle`, covering valid and invalid construction,
all four boundaries, overlap, contact in all eight directions, hits, misses, repeated
shots, sinking, victory and restart.

The generator posed a problem worth describing. `Board` hides where the ships are,
correctly for a game, but that left no simple way to check a generated fleet. Rather
than weaken the class with an accessor only tests would use, the checks fire at all
hundred cells and rebuild the ships as groups of hit cells that touch. That is valid
only if ships never touch, so finding six groups of sizes four, three, three, two,
two and two proves composition, absence of overlap and the separation rule at once.

Writing checks first also found a defect: a board too small for the Battleship
produced `IllegalArgumentException: bound must be positive` from inside the Java
library, because a non-positive bound reached `Random.nextInt`. The generator now
refuses a ship longer than the board first.

## 6.2 Functional testing

Unit checks cannot show the interface behaves correctly, so the finished game was
also tested by clicking the grid as a player would.

| # | Action | Expected | Observed |
| --- | --- | --- | --- |
| F1 | Start the application | 100 unexplored cells, no shots, six ships | Pass, Fig. 1 |
| F2 | Click an empty cell | Blue; shots +1, hits unchanged | Pass, Fig. 2 |
| F3 | Click a ship cell | Red; shots +1, hits +1 | Pass, Fig. 2 |
| F4 | Click a cell already fired at | Nothing changes | Pass; cell disabled |
| F5 | Hit a ship's last cell | Dark red, ships left falls, `Sunk!` | Pass, Fig. 3 |
| F6 | Sink the whole fleet | Victory dialog and total shots | Pass, Figs. 4, 5 |
| F7 | Click after victory | Nothing happens | Pass; grid disabled |
| F8 | Press New Game | Board and counters cleared, new fleet | Pass, Fig. 6 |
| F9 | Count the revealed fleet | One 4, two 3s, three 2s, none touching | Pass, Fig. 4 |

Two defects were found this way, both invisible to every automated check. The first
interface drew a typographic symbol on each fired-at cell and every one appeared as
an empty box, because the look and feel's font had no glyph for them. Switching to
plain ASCII revealed a second fault: the marks then showed as `...`, because a Swing
button's wide default margin left too little room for one character. Setting the cell
margin to zero fixed it. Unit checks can confirm a colour is blue but not that a
character is legible, which is why both kinds of testing were needed.
