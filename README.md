# Battleship Java

MOD004883 Advanced Object-Oriented Programming coursework.

**Current state: Coordinate and Ship domain models; MVP development planned.** Code, architecture, report drafts and test evidence are maintained together here.

Start with [documentation](docs/README.md), [architecture](docs/ARCHITECTURE.md), and [MVP roadmap](docs/ROADMAP.md).

## Run (JDK 11+)

```bash
mkdir -p out
javac -d out src/main/java/battleship/model/*.java src/main/java/battleship/App.java src/test/java/battleship/model/*.java
java -cp out battleship.App
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
```

No external dependencies. Self-checks are not JUnit. The intended MVP is a 10×10 Swing game with random non-touching fleet placement, hit/miss feedback, victory and restart. It is not implemented yet.

Report sources live under docs/report. Preserve docs/planning as the initial baseline and record subsequent decisions honestly. Final Canvas deliverables remain a Word report and a separate Java ZIP. Teaching slides and unrelated sample projects are not redistributed here.
