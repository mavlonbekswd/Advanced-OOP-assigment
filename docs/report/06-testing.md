# 6. Testing

*Target: 300 words. Status: the unit-testing half is complete; functional testing is
blocked on the interface.*

---

## 6.1 Approach

Testing was carried out alongside development rather than at the end. Each class was
checked before the next one was written, so that a fault in the rules could never be
mistaken for a fault in a later layer. Checks were written before the code they test,
and each was observed failing before the code that satisfies it was written. This
matters because a check that has never failed proves very little: it may be testing
the wrong thing.

## 6.2 Unit testing

The checks are plain Java classes with a `main` method rather than JUnit tests. This
keeps the project free of external dependencies, so it compiles and runs with nothing
but a JDK. The honest limitation is that these harnesses are not a recognised testing
framework, and they stop at the first failure instead of reporting every failure at
once.

One hundred and fifty-eight checks pass in total: ten for `Coordinate`, fifty-two for
`Ship`, forty-three for `Board`, twenty-one for `RandomShipPlacer` and thirty-two for
`GameController`. They cover valid and invalid construction, boundaries in all four
directions, overlap, contact in all eight directions, hits, misses, repeated shots,
sinking, victory and restart.

The fleet generator posed a problem worth describing. `Board` deliberately hides
where the ships are, which is correct for a game, but it left no simple way to check
a generated fleet. Rather than weakening the class by adding an accessor that only
tests would use, the checks fire at all one hundred cells and rebuild the ships as
groups of hit cells that touch each other. That reconstruction is only valid if ships
never touch, so finding exactly six groups of sizes four, three, three, two, two and
two proves the fleet composition, the absence of overlap and the separation rule in a
single assertion.

Writing checks first also found a real defect. A check that a board too small to hold
the Battleship should fail cleanly instead produced `IllegalArgumentException: bound
must be positive` from inside the Java library, because a non-positive bound was
reaching `Random.nextInt`. The generator now refuses a ship longer than the board
before it chooses a position.

## 6.3 Functional testing

**PENDING — blocked on the interface.**

This section needs a table of test cases carried out by hand on the running
application: each row giving the action, the expected result and the observed result.
The cases should at least cover clicking an empty cell, clicking a ship, clicking the
same cell twice, sinking a ship, sinking the whole fleet and pressing Restart.
