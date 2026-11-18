# Battleship Java

MOD004883 Advanced Object-Oriented Programming coursework.

**Current state: Coordinate, Ship and Board domain models plus random fleet generation; controller and interface planned.** Code, architecture, report drafts and test evidence are maintained together here.

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
```

No external dependencies. Self-checks are not JUnit; 126 of them pass. The intended MVP is a 10×10 Swing game with random non-touching fleet placement, hit/miss feedback, victory and restart. Fleet generation now works; the playable game does not exist yet.

Report sources live under docs/report. Preserve docs/planning as the initial baseline and record subsequent decisions honestly. Final Canvas deliverables remain a Word report and a separate Java ZIP. Teaching slides and unrelated sample projects are not redistributed here.
