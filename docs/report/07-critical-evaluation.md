# 7. Critical Evaluation

*Target: 330 words. Status: complete.*

---

## 7.1 Does the application meet the requirements?

Every requirement from the brief is met, and both chosen extensions work. The
strongest evidence for the placement rules is Figure 4: when a game ends the sixteen
ship cells become visible and the fleet can be counted on screen, so a requirement
otherwise visible only to automated checks can be confirmed by eye.

One requirement was interpreted rather than simply met. The brief does not say
whether ships meeting at a corner are touching. This project treats diagonal contact
as touching, the stricter reading, so every layout it produces would also be legal
under the looser one; but it reduces the number of possible layouts and is an
interpretation rather than a fact.

## 7.2 Was a plan made, and was it followed?

A planning document was written before any application code existed and preserved
unchanged. The order of work followed it: model, generator, controller, interface,
each tested before the next began, and a development log records what was actually
done at each stage, including what went wrong. The plan was not followed blindly:
four differences are recorded in section 4.4 with their reasons.

## 7.3 Limitations

The checks are plain Java classes rather than JUnit tests: no dependencies, but not
a recognised framework, and they stop at the first failure. The window itself has no
automated coverage; its correctness rests on nine hand-run functional cases and on
holding no rule of its own, and the two rendering faults in section 6.2 show the risk
in that.

`Board` does not enforce the fleet composition, accepting any ships that fit the
placement rules. This lets tests use a board of one or two ships, but a future caller
could build a wrong fleet unchallenged. Finally, nothing is saved, and the difficulty
levels and two-player mode the brief offers were deliberately deferred.

## 7.4 Object-oriented concepts

Of the fourteen concepts the brief lists, expecting most but not all, this project
demonstrates twelve; they are listed in section 4.2.

Three are absent by decision rather than oversight. No abstract class is used,
because nowhere do two implementations share partial behaviour. No design pattern was
deliberately introduced: the observer relationship between a button and its listener
comes from Swing. Mutator methods are almost entirely absent, because `Coordinate`
and a ship's positions are immutable; encapsulation was taken to mean controlling
change, not providing a setter for every field.

## 7.5 What could be improved

Converting the checks to JUnit would come first, since it is the gap most likely to
cost marks while the behaviour checked would not change. After that, naming the type
of ship when one is sunk, and adding difficulty levels through the board-size
parameter that already exists and is already tested.
