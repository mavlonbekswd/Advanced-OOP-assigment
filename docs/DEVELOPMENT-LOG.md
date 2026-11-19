# Development log

## 2026-11-03 — Repository preparation
Imported the existing v0.1 starter and unchanged initial Word planning document. Added requirements traceability, roadmap through MVP, proposed UML, report scaffold and testing plan. No complete game or GUI is claimed. GitHub publication was pending at preparation time.

## 2026-11-14 — GitHub import
Imported the prepared starter, architecture, roadmap, test plan and report scaffold into the user-created Advanced-OOP-assigment repository. MVP implementation remains planned. javac is unavailable in this environment, so the Coordinate self-checks were not rerun during this import.

## 2026-11-14 — Ship domain milestone
Added ShipType and Ship, immutable validated positions, distinct hit tracking and sunk state. Chose composition over subclasses because ship types currently differ only by length. Added 52 dependency-free Ship checks. Ran these plus 10 Coordinate checks successfully using the JDK 21 compiler module with source/target 11. The runtime lacks Java 11 release signatures, so this is not verification against the full Java 11 API. Board remains next.

## 2026-11-15 — Board domain milestone
Implemented Board and ShotResult, copied ship ownership, bounds and diagonal separation rules, immutable shot snapshots, setup closure after first shot, duplicate detection and nonempty-fleet victory checks. Ran 10 Coordinate, 52 Ship and 43 Board checks successfully on Java 21. Random fleet generation is next; no playable GUI yet.

## 2026-11-18 — Random fleet placement milestone
Added `battleship.service.RandomShipPlacer`, the first class outside the model
package. It owns the required fleet composition and chooses random positions, while
`Board` stays the only authority on whether a placement is legal. The source of
randomness is injected through a constructor so fleets are reproducible from a seed.
Generation is bounded: 200 position attempts per ship and 100 whole-board attempts,
after which an `IllegalStateException` is raised instead of looping.

Written test first. The checks were written against the public board API before the
class existed, and the compilation failure naming the missing class was observed. A
later check, that a board too small to hold the Battleship fails cleanly, exposed a
real defect: the generator passed a non-positive bound to `Random.nextInt` and
surfaced `IllegalArgumentException: bound must be positive` from inside the JDK. That
failure was observed before the fix.

Added 21 placement checks, giving 126 in total, all passing on OpenJDK 23. Also added
in-code comments naming the OOP concepts used in `Ship`, `Board`, `ShipType` and
`ShotResult`; the assessment asks for these explicitly and only `Coordinate` had them.
No behaviour was changed by those comments. Controller and interface remain unbuilt.

## 2026-11-18 — Game controller milestone
Added `battleship.controller.GameController`, completing the model, service and
controller layers. It owns starting and restarting a game, taking shots, counting
shots and hits, and refusing play once the fleet is destroyed. It exposes no board
object, only what an interface needs in order to draw.

Written test first, in two cycles. The first checks described a controller that had
not started a game and were watched failing on the missing class. The second
described the shot counters, the duplicate rule, invalid input, victory and restart,
and were watched failing on `First shot counted`, because the first version returned
the board's outcome without recording anything.

Added 32 controller checks, giving 158 in total, all passing on OpenJDK 23. Also
cleared every outstanding javadoc warning across the project: the enum constants had
no comments at all and several methods carried an `@return` tag with no description.
The assessment marks javadoc explicitly, and `javadoc` now reports zero warnings.
Only the Swing interface remains before the MVP is playable.

## 2026-11-18 — Swing interface milestone
Added `battleship.view.CellStyle` and `battleship.view.GameWindow`, and replaced the
placeholder `App` with one that opens the game. The application is now playable: a
ten by ten grid of clickable cells, blue misses, red hits, a darker red for a sunk
ship, a running status line, a victory dialog and a New Game button.

The view was deliberately split in two so that part of it could be tested. `CellStyle`
is pure data and was written test first; the checks that a miss is blue and a hit is
red were watched failing on the missing class. `GameWindow` is not unit tested, since
it contains no decision of its own.

Running the application found two faults that no automated check could have caught.
The marks drawn on fired-at cells first appeared as empty boxes, because the look and
feel's font had no glyph for the typographic symbols chosen. After replacing them
with ASCII they appeared as `...`, because a Swing button's default margin left too
little width inside a cell this small and the text was clipped to an ellipsis. Setting
the cell margin to zero fixed it.

Captured six screenshots of the real application and recorded nine functional test
cases. 12 new checks, 170 in total, all passing on OpenJDK 23 with no compiler or
javadoc warnings. The evidence tool that drives the window is kept in docs/evidence,
outside src, so it is not mistaken for application code.

## 2026-11-18 — Sources resolved and report trimmed
The Week 3 and Week 4 slides and the interview question sheet were supplied, which
settles three things left open since the first audit.

The word limit is confirmed by Week 4: 3,000 words for the component, about
1,500-2,000 for the report, up to 3,000 maximum. Whether captions, tables and the
appendix count towards it is still unstated anywhere.

The Week 3 lecture supports the decision not to subclass the ship types, saying a
ship hierarchy is not really necessary for an example this simple. That is now cited
in the report rather than argued from first principles alone. The same lecture names
the fleet differently from the assessment brief, confirming the conflict recorded in
the planning document; the brief remains authoritative.

The interview sheet gives ten questions and states that the answers must be the
student's own and must not be generated by a language model. The appendix therefore
records the questions with empty answer cells, together with pointers to places in
this project that can supply a genuine example for seven of the ten. No answers were
written.

Trimmed the report from about 4,480 words of prose to 2,596. The index records what
would have to be moved to reach 2,000 and notes that doing so means relocating
content that carries marks.

## 2026-11-18 — UML diagrams drawn
Drew the four diagrams the brief requires in Lucidchart and exported them to
`docs/diagrams/`: the initial use case, sequence and class models for the planning
section, and the final class model for the implementation section. The Lucid
documents stay editable, so a correction does not mean redrawing.

Importing Mermaid into Lucid turned out to store the source as a code block rather
than as shapes, so those documents could not be exported as images. The class and use
case diagrams were rebuilt from native Lucid UML shapes instead; the sequence diagram
came out correctly from PlantUML the first time. The two unusable documents are
recorded in `docs/diagrams/README.md` so they can be deleted.

Report sections 2.3, 2.4, 2.5 and 4.3 now carry the figures and a short commentary
each, replacing the pending notices.

## 2026-11-18 — Diagrams restyled for consistency
The four diagrams were drawn in two different styles: three in plain black and white
and the sequence diagram in the blue its generator always produces. The sequence
generator cannot be forced into black and white, so the other three were redrawn in
the same blue instead. One consistent set matters more than which of the two styles
it is, since the lecture slides ask for a consistent document and neither the brief
nor the slides mention colour at all.

Colour now carries meaning rather than decoration: solid blue for what this project
wrote, dashed grey for what is outside it (Swing's JFrame, and the use case the
player never performs), and a filled actor. Nothing depends on colour alone, so the
figures still read when printed in black and white. The earlier black and white
exports are kept in docs/diagrams/bw.

## 18 November 2026, evening — references, figure numbering and the Word document

The references were written in Harvard form. Before citing any lecture, the slide
files were opened and searched to confirm that the claim attributed to them is
actually there: Week 3 on the ship hierarchy, Week 4 on the word limit and the
extensions, Week 5 on documentation, Week 6 on the MVC example and on the difference
between functional and non-functional testing, Week 7 on DRY. Two vague citations in
section 3 that said only "the lectures" now name Week 6 and Week 7. Weeks 1, 2, 8 and
9 were read during the module but are not cited, so they are not listed.

Every figure was renumbered into one sequence, 1 to 11, so that Word can build a
table of figures. The diagram files keep their letters A to D; the mapping is in
`docs/diagrams/README.md`.

A screenshot turned out to be worthless as evidence. `06-after-restart.png` is
byte-identical to `01-new-game.png` — which is correct behaviour, since a cleared
board looks exactly like a board that has never been played, but it means the old
caption, "a fresh fleet is hidden in new positions", claimed something the picture
cannot show. `Capture.java` was extended to play the second game to its end as well,
and the new `07-second-victory-board.png` shows a different fleet in different cells,
won in ninety-two shots. That is the report's visible evidence for random placement,
and it is recorded as functional test case F10. The first attempt at the capture
appeared to do nothing; the cause was only that the second game needs longer than the
1.5 seconds the first one was given before the window is photographed.

`build-docx.js` assembles the submission document: cover page, table of contents,
table of figures, numbered headings, eleven captioned figures, Arial throughout, page
numbers, and the interview questions and functional tests as appendices A and B. The
captions use Word SEQ fields so the table of figures can collect them, which means
the fields have to be updated once when the document is first opened.

Three of the four planned trims were made in the process: the functional test table
moved to Appendix B, section 4.2 became a table, and the screenshot commentary became
figure captions. Sections 1 to 8 now come to 2,670 words. The fourth trim, cutting
the requirements in section 2.1, was not made, because planning carries 25%.

All 170 self-checks still pass, and `javac -Xlint:all` is clean.

## 19 November 2026 — difficulty settings, a fleet panel, and the report brought back in line

The interface was rebuilt around three pieces instead of one: `BoardPanel` draws the
labelled grid and reports a click through a `ShotListener` it declares itself,
`FleetPanel` lists the enemy ships and the counts, and `GameWindow` assembles them and
holds the toolbar and the status line. `CellStyle` was left alone, so its twelve
checks and the ASCII-mark history behind them still stand.

Two classes were added to the model. `Difficulty` pairs a board size with a shot
limit (Easy 12 and 144, Normal 10 and 70, Hard 8 and 40), and `ShipStatus` carries a
ship's type and whether it has sunk and nothing else. The second exists because the
fleet panel has to name the ships that have gone down without being told where any
ship is; `Board.getFleetStatus()` reduces each ship to one of these, so the rule that
a ship never leaves the board survived a feature that appeared to need it broken.

The shot limit is counted in `GameController`, not in `Board`, because how many shots
a player is allowed is a fact about one session of play. That also gave the game a
state it had never had: it can now be lost. `isWon()`, `isLost()` and `isGameOver()`
make the three outcomes explicit, and a shot at a finished game is refused.

Easy allows exactly one shot per square on purpose. It cannot be lost, which makes it
the only setting where a check that fires at every square is certain to end in a win,
and those sweep-based checks were moved onto it. Defeat is checked the other way
round: ten seeded games played out on Hard, confirming that each one ends, that it is
won or lost and never both, and that the shots never exceed the limit. The suite went
from 170 checks to 296.

The report then had to be brought back in line with the code, which was most of the
work. Every screenshot was retaken, because the old set could not be reproduced: with
a limit of seventy shots, clicking every square in turn no longer wins on Normal. The
capture harness now plays the way a person does, firing on every other square and then
working outwards from a hit, and the new Figure 8 shows a game won in fifty-six shots
with a third of the board never touched. Three figures were added for the difficulty
setting, Figure 4 was redrawn with the fourteen classes the code now has, and five
functional cases (F11 to F15) were run against the real window and recorded.

One old claim turned out to be wrong rather than merely out of date. The caption for
the sunk-ship figure said both squares of a destroyed Destroyer turn the darker red.
They do not: only the square that completes the sinking does, because each square
keeps the outcome of the shot that struck it, and that was true of the first version
too. The caption now describes what the picture shows, section 7.3 records it as a
limitation, and section 7.5 proposes re-marking the whole ship as the fix.

Adding all of this took the document from 2,670 words to 3,614, past the 3,000 limit.
The concepts table moved from section 4.2 to a new Appendix C, as the functional test
table had already moved to Appendix B, and every section was tightened; the document
now stands at 2,951 words with fourteen captioned figures.

## 19 November 2026, later — the cell marks, properly this time

The grid went back to drawn symbols: a bullet for a miss, a cross for a hit and a
filled square for a sinking. The first version of the interface had tried symbols,
found that the look and feel's font had no glyph for them, and retreated to `o`, `X`
and `#`. Retreating was the right call at the time, but it left the real problem
unanswered: the code was still assuming what a font can draw, it had just picked an
assumption that happens to hold.

`CellStyle.marksFor(Font)` answers the question instead of assuming. It takes the
font that will actually draw the text, asks it about all three symbols, and returns
`PLAIN` unless every one of them has a glyph. `labelFor` then takes that answer as an
argument, so the class stays pure data with no Swing lookup inside it and both paths
can be checked with no display open. `BoardPanel` asks once per redraw rather than
once per square, since every square carries the same font.

It is deliberately all three symbols or none. A grid showing two symbols and one
letter would look like a bug rather than a fallback.

`Capture.java` no longer hard-codes the marks either; it asks `CellStyle` what a hit
looks like, so the harness cannot drift out of step with the grid it is reading.

The CellStyle harness grew from 12 checks to 29, putting the suite at 313. The
screenshots were retaken; the seeded games are identical, so every number already
quoted in the report still holds and only the marks in the pictures changed.

## 19 November 2026, evening — making the marks readable

The marks were being drawn in the look and feel's disabled-text grey, because a
square is disabled as soon as it has been fired at. That grey measures 1.29 to 1
against the blue of a miss and 1.48 against the red of a hit, where the accessibility
guidelines ask 3 to 1 of large text; only the dark red of a sunk square came close,
at 3.16. The marks exist for a player who cannot tell the colours apart, so a mark
that is itself hard to see defeats the point of having one.

White gives 3.68, 4.23 and 9.01 on the three colours. `CellStyle.markColour()` holds
that decision with the rest of the appearance, and three new checks measure the
ratios rather than asserting a colour, which is the part worth keeping: the check
would still be right if the cell colours changed.

Getting the colour onto a disabled button took a measurement rather than a guess.
`setForeground` is ignored on a disabled button, and so is an HTML colour inside the
label, which was tried and rendered in the same grey. The only thing that works is
replacing the look and feel's `Button.disabledText` default, so `BoardPanel` does
that: it is the class that disables squares, so it is the class that answers for how
a disabled square reads.

The screenshots were retaken a third time. The seeded games came out identical once
more, so again no number in the report moved and only the pictures changed.
