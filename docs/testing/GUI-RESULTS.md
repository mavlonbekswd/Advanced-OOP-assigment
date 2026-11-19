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

---

# Interface test evidence — 19 November 2026

The window was rebuilt around a toolbar, a labelled grid and a fleet panel, and a
difficulty setting was added, so this is a fresh run rather than an amendment of the
record above. The figure numbers used here are the report's new ones (5 to 14).

## Automated checks

```bash
rm -rf build && mkdir -p build
javac -Xlint:all -d build $(find src/main/java -name '*.java')
javac -d build -cp build $(find src/test/java -name '*.java')
java -cp build battleship.model.CoordinateSelfTest
java -cp build battleship.model.ShipSelfTest
java -cp build battleship.model.BoardSelfTest
java -cp build battleship.model.DifficultySelfTest
java -cp build battleship.service.RandomShipPlacerSelfTest
java -cp build battleship.controller.GameControllerSelfTest
java -cp build battleship.view.CellStyleSelfTest
```

```
PASS: 10 Coordinate self-checks
PASS: 52 Ship self-checks
PASS: 55 Board self-checks
PASS: 26 Difficulty self-checks
PASS: 21 RandomShipPlacer self-checks
PASS: 120 GameController self-checks
PASS: 12 CellStyle self-checks
```

296 checks in total on OpenJDK 23.0.2, macOS. No compiler warnings under
`-Xlint:all` and no `javadoc` warnings.

## Functional cases

F1 to F10 were re-run against the rebuilt window and all still pass; the screenshots
in `docs/screenshots/` were retaken from it. F11 to F15 are new and cover the
difficulty setting. What the window reported in each case:

| # | Action | Observed |
| --- | --- | --- |
| F11 | Choose Easy | 144 squares, counter reads `0 / 144`, labels run A to L and 1 to 12 |
| F12 | Choose Hard | 64 squares, counter reads `0 / 40` |
| F13 | Use every shot on Hard | Dialog: "You have used all 40 shots and 3 ship(s) survived"; grid disabled |
| F14 | Change the setting two shots into a game | Confirmation dialog appears; answering No leaves the game on Hard at `2 / 40` with the status line unchanged, and the chooser returns to Hard |
| F15 | Press Rules | Dialog explaining the fleet, the colours and the shot limit |

F14 is the case worth keeping: the chooser is the only control that can destroy a game
in progress by accident, and it is the only one that asks first.

One behaviour was confirmed rather than found: when a ship sinks, only the square that
completes it turns the darker red. The earlier caption for the sunk-ship figure said
both squares of a Destroyer darken, which the picture never showed. This is recorded
as a limitation in report section 7.3.

## 19 November 2026, later — the cell marks

The marks on a fired-at square were changed from `o`, `X` and `#` to a bullet, a
cross and a filled square (U+2022, U+2715, U+25A0). The colours are untouched: the
brief fixes blue for a miss and red for a hit.

This is a return to the symbols the first version tried and had to abandon, but it is
not the same decision. `CellStyle.marksFor(Font)` asks the font whether it can draw
all three symbols before any of them is used, and answers `PLAIN` when it cannot, so
the ASCII marks remain as a fallback rather than as a retreat. It is all three or
none: a grid showing two symbols and one letter would read as a mistake.

```
PASS: 29 CellStyle self-checks      (was 12)
313 checks in total
```

The seventeen new checks cover both sets, that the three marks differ within each
set, that every mark is exactly one character so no square has to shrink its text,
that `marksFor(null)` is `PLAIN`, and that `SYMBOLS` is chosen only when the font can
in fact draw them. The last of these asks the font directly rather than trusting
`CellStyle`, so it is not simply repeating the class under test.

On this machine the look and feel's button font is the logical font `Dialog`, which
can draw all three, so the running game uses the symbols; the screenshots in
`docs/screenshots/` show them. The fallback path was therefore exercised by the
checks rather than by the display, which is worth saying plainly: no machine has been
observed falling back.

The marks are still drawn in the look and feel's disabled-text grey, because a square
is disabled once it has been fired at. Against the red of a hit that is readable but
not strong, and it was the same before this change.
