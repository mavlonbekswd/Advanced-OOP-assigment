# Battleship Java

MOD004883 Advanced Object-Oriented Programming coursework.

**Current state: model, random fleet generation and game controller are implemented and tested; the user interface is not built, so the game is not yet playable.** Code, architecture, report drafts and test evidence are maintained together here.

Start with [documentation](docs/README.md), [architecture](docs/ARCHITECTURE.md), and [MVP roadmap](docs/ROADMAP.md).

## Run (JDK 11+)

```bash
mkdir -p out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out battleship.App
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
java -cp out battleship.service.RandomShipPlacerSelfTest
java -cp out battleship.controller.GameControllerSelfTest
```

No external dependencies. Self-checks are not JUnit; 158 of them pass. The intended MVP is a 10×10 Swing game with random non-touching fleet placement, hit/miss feedback, victory and restart. The rules, the fleet generator and the game lifecycle all work and are tested; nothing draws them yet, so there is no playable game and no screenshot evidence.

Report sources live under docs/report. Preserve docs/planning as the initial baseline and record subsequent decisions honestly. Final Canvas deliverables remain a Word report and a separate Java ZIP. Teaching slides and unrelated sample projects are not redistributed here.
