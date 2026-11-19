# Battleship Java

MOD004883 Advanced Object-Oriented Programming coursework.

**Current state: the game is playable and the report is assembled. Model, random fleet generation, controller, difficulty settings and the Swing interface are implemented and tested; what remains is the student's own interview answers and the cover details.** Code, architecture, report drafts and test evidence are maintained together here.

Start with [documentation](docs/README.md), [architecture](docs/ARCHITECTURE.md), and [MVP roadmap](docs/ROADMAP.md).

## Run (JDK 11+)

```bash
mkdir -p out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out battleship.App
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
java -cp out battleship.model.DifficultySelfTest
java -cp out battleship.service.RandomShipPlacerSelfTest
java -cp out battleship.controller.GameControllerSelfTest
java -cp out battleship.view.CellStyleSelfTest
```

No external dependencies. Self-checks are not JUnit; 313 of them pass, and `javadoc` reports no warnings. `battleship.App` opens the game: a labelled Swing grid with random non-touching fleet placement, blue misses, red hits, an enemy-fleet panel, a victory message and New Game. A difficulty setting changes the board size and the number of shots allowed, so a game can also be lost. Screenshots of the running application are in docs/screenshots.

Report sources live under docs/report. Preserve docs/planning as the initial baseline and record subsequent decisions honestly. Final Canvas deliverables remain a Word report and a separate Java ZIP. Teaching slides and unrelated sample projects are not redistributed here.
