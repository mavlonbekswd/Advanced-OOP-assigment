# Game controller test evidence — 18 November 2026

Compiled and ran every self-test main class from a clean `out/` directory:

```bash
rm -rf out && mkdir -p out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out battleship.model.CoordinateSelfTest
java -cp out battleship.model.ShipSelfTest
java -cp out battleship.model.BoardSelfTest
java -cp out battleship.service.RandomShipPlacerSelfTest
java -cp out battleship.controller.GameControllerSelfTest
```

```
PASS: 10 Coordinate self-checks
PASS: 52 Ship self-checks
PASS: 43 Board self-checks
PASS: 21 RandomShipPlacer self-checks
PASS: 32 GameController self-checks
```

158 checks in total, on OpenJDK 23.2 on macOS. Compilation produced no errors and no
`-Xlint:all` warnings, and `javadoc` now reports no warnings for any class.

## Coverage

Lifecycle:
- A controller that has not started a game reports so, is not "over", and refuses a
  shot, a board size request and a fleet count.
- `newGame` produces a standard board with six ships afloat, zero shots, zero hits
  and an empty shot history.
- `newGame` called again clears counters, history and the finished state, so Restart
  is a real restart rather than a continuation.

Shooting:
- A shot at a fresh cell increments the shot count once, and increments the hit count
  only when it struck a ship.
- A second shot at the same cell returns `ALREADY_SHOT` and changes neither count.
  This is the duplicate-shot rule from the planning document.
- A cell outside the board, in either direction, and a null cell are rejected, and
  leave every count unchanged.

Victory:
- Playing every cell of a seeded game sinks the whole fleet; the controller then
  reports the game over, exactly 16 hits and no ships afloat.
- After victory every further shot is refused, including one at a cell never fired
  at, and the counts stay at their final values.

Reproducibility:
- Two games generated from the same seed produce an identical sequence of outcomes
  across the whole board. This is what lets these checks assert an exact hit count of
  16 rather than a range.

## Limitations

These are plain Java harnesses, not JUnit, and they exercise the controller directly
rather than through a user interface. No Swing window exists yet, so there is still
no functional test of mouse input, cell colours or an on-screen victory message, and
no screenshot evidence. The red and blue colours required by the brief are still
unimplemented: the controller reports outcomes, but nothing draws them.
