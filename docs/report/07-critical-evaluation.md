# 7. Critical Evaluation

*Target: 300 words. Status: complete, written after the application was finished.*

---

## 7.1 Does the application meet the requirements?

Every requirement taken from the brief is met. The game presents a ten by ten grid
hiding one Battleship, two Cruisers and three Destroyers; the computer places them
with no overlap and no contact; a selected square turns blue when empty and red when
it covers a ship; and the game ends once every ship has been sunk. The two chosen
extensions, random placement and a mouse-controlled interface, are also working.

The strongest evidence for the placement rules is Figure 4. When a game ends, the
sixteen ship cells become visible, and the fleet can be counted directly on screen:
one line of four, two of three and three of two, with no two ships meeting at an
edge or a corner. A requirement that is otherwise only visible to automated checks
can therefore be confirmed by eye.

One requirement was interpreted rather than simply met. The brief says two ships must
not touch but does not say whether meeting at a corner counts. This project treats
diagonal contact as touching. That is the stricter reading, so every layout it
produces would also be legal under the looser one, but it does reduce the number of
possible layouts and should be acknowledged as an interpretation rather than a fact.

## 7.2 Was a plan made, and was it followed?

A planning document was written before any application code existed and has been
preserved unchanged. The order of work followed the plan: the domain model, then the
fleet generator, then the controller, then the interface, with each stage tested
before the next began. A development log records what was actually done at each
stage, including the things that went wrong.

The plan was not followed blindly. Four differences from it are recorded in section
4.7, each with the reason it was made. The most significant is that the three ship
types were not made into subclasses, because they differ only in length and a
subclass that adds no behaviour adds only complexity.

## 7.3 Limitations

The honest weaknesses of this submission are as follows.

The automated checks are plain Java classes with a `main` method rather than JUnit
tests. This keeps the project free of dependencies, but they are not a recognised
framework and they stop at the first failure instead of reporting everything at once.

The window itself has no automated coverage. Its correctness rests on nine functional
cases carried out by hand and on the fact that it contains no rule of its own. The
two rendering faults described in section 6 show the risk in that: both were
invisible to every automated check and were found only by looking at the screen.

`Board` does not enforce the fleet composition; it accepts any ships that fit the
placement rules, and the exact fleet is the generator's responsibility. This is
convenient, because tests can use a board holding one or two ships, but it means a
future caller could build a board with the wrong fleet and the board would not object.

Nothing is saved. Closing the window loses the game, and there is no record of
previous results. The difficulty levels and the two-player mode offered by the brief
were deliberately deferred and are not present.

## 7.4 Object-oriented concepts

Of the fourteen concepts listed in the brief, which expects most but not all, this
project demonstrates encapsulation, abstraction, inheritance, polymorphism, state,
constructors, accessor methods, interfaces, overriding, overloading, an architectural
pattern and a justified choice of collections.

Three are not demonstrated, and each absence was a decision rather than an oversight.
No abstract class is used, because no place in this design has two implementations
that share partial behaviour. No design pattern was deliberately introduced; the
observer relationship between a button and its listener comes from Swing rather than
from this project's design. Mutator methods are almost entirely absent, because
`Coordinate` and the positions of a `Ship` are immutable; encapsulation was taken to
mean controlling change, not providing a setter for every field.

## 7.5 What could be improved

Converting the checks to JUnit would be the first improvement, since it is the one
gap most likely to cost marks and the behaviour being checked would not change.
Beyond that, the most useful additions in order of value would be: naming the type of
ship in the message when one is sunk, which the controller does not currently report;
difficulty levels using the existing board-size parameter, which already exists and
is tested; and keyboard control alongside the mouse, which would make the game usable
without pointing.
