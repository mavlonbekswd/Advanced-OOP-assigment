# Interface test evidence — 18 November 2026

## Automated checks

```bash
rm -rf out && mkdir -p out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
java -cp out battleship.service.RandomShipPlacerSelfTest
java -cp out battleship.controller.GameControllerSelfTest
java -cp out battleship.view.CellStyleSelfTest
```

```
PASS: 10 Coordinate self-checks
PASS: 52 Ship self-checks
PASS: 43 Board self-checks
PASS: 21 RandomShipPlacer self-checks
PASS: 32 GameController self-checks
PASS: 12 CellStyle self-checks
```

170 checks in total on OpenJDK 23.0.2, macOS. No compiler warnings under
`-Xlint:all` and no `javadoc` warnings.

Only the appearance of a cell is checked automatically, because it is pure data:
given what the player knows about a cell, `CellStyle` returns a colour and a mark.
The checks confirm that a miss is predominantly blue and a hit predominantly red, as
the brief requires, that a sunk cell is a darker red than a hit, and that asking for
the appearance of `ALREADY_SHOT` is rejected, since that value is the answer to a
repeated shot and never the state of a cell. No window is opened, so these run
anywhere.

## Functional cases

Carried out on the running application by clicking the grid. Screenshots of the
states reached are in `docs/screenshots/`.

| # | Action | Expected | Observed |
| --- | --- | --- | --- |
| F1 | Start the application | Grid of 100 unexplored cells; no shots, six ships | Pass, Figure 1 |
| F2 | Click an empty cell | Blue, shots +1, hits unchanged | Pass, Figure 2 |
| F3 | Click a ship cell | Red, shots +1, hits +1 | Pass, Figure 2 |
| F4 | Click a cell already fired at | Nothing changes | Pass; cell disabled after its first shot |
| F5 | Hit a ship's last cell | Ship turns dark red, ships left falls, status shows Sunk! | Pass, Figure 3 |
| F6 | Sink the whole fleet | Victory dialog, status reports total shots | Pass, Figures 4 and 5 |
| F7 | Click after victory | Nothing happens | Pass; the grid is disabled |
| F8 | Press New Game | Board and counters cleared, new fleet generated | Pass, Figure 6 |
| F9 | Count the fleet revealed at the end | 1 line of 4, 2 of 3, 3 of 2, none touching | Pass, Figure 4 |

## Defects found by functional testing

Two faults were found that the automated checks could not have caught, because both
concern whether a mark is legible rather than what colour it is.

The first version drew a typographic symbol on each fired-at cell. Every one appeared
on screen as an empty box: the font of the chosen look and feel had no glyph for
those characters. They were replaced with plain ASCII.

That exposed a second fault: the marks then appeared as `...`. A Swing button keeps a
wide default margin, which inside a cell this small left too little width for even
one character, so the text was being shortened to an ellipsis. Setting the cell
margin to zero fixed it, and the marks `o`, `X` and `#` now render correctly.

## Limitations

The window itself is not covered by automated checks. Its correctness rests on the
functional cases above, which were carried out by hand, and on the fact that it
contains no rule of its own: it reports clicks to the controller and redraws what the
controller reports.

The harness in `docs/evidence/` drives the real application through its own buttons
to capture the screenshots. It is evidence tooling, not part of the game, and is kept
outside `src/` so it is not mistaken for application code.
