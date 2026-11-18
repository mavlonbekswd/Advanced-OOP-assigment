# 2. Requirements and Planning

*Target: 400 words plus three UML models. Status: prose complete; diagrams pending.*

---

## 2.1 Requirements taken from the brief

The brief fixes the rules, and these were treated as non-negotiable. The player is
shown a ten by ten grid hiding one Battleship of four cells, two Cruisers of three
and three Destroyers of two: six ships occupying sixteen of the hundred cells. The
computer must place them so that no two overlap and no two touch. A selected square
turns blue if empty and red if it covers a ship, and the game ends once every ship is
sunk.

The brief also sets two thresholds. A basic game, with fixed ship positions and typed
coordinates, cannot score above forty per cent; to pass fifty it lists several
extensions. Two were chosen: random placement and a mouse-controlled interface, both
of which the Week 4 lecture also names as expected. Difficulty levels and a
two-player mode were deliberately deferred, because a smaller application that works
completely was judged more valuable than a larger unfinished one.

Two points required a decision. First, the brief says ships must not touch but not
whether meeting at a corner counts; this project treats diagonal contact as touching.
The stricter rule cannot produce a layout the looser rule would reject, so it is safe
under either reading and is simple to test. Second, the Week 3 lecture illustrates
the same game with a differently named fleet. The assessment brief is authoritative,
so its composition was followed.

## 2.2 How the work was planned

As the work of one person, development followed a waterfall sequence rather than an
iterative team process, as the Week 5 lecture advises: requirements, then
architecture, then code, then tests of each piece as it was finished.

A planning document was produced before any application code existed, recording the
requirements, separating what the brief demands from what this project chose to add,
listing assumptions needing confirmation and setting out an initial architecture. It
has been preserved unchanged, so the differences between plan and outcome can be
shown honestly in section 4.4.

The work was divided into stages that could each be finished and tested alone: the
model, the generator, the controller, then the interface. Each had to pass its own
checks before the next began, so a fault in the rules could never be mistaken for a
fault in the interface.

## 2.3 Initial use case model

**PENDING — diagram to be drawn.** Actor: the human player. Use cases: start a game,
select a target, see hit or miss feedback, see victory, restart. Random placement
occurs as part of starting a game.

## 2.4 Initial sequence model

**PENDING — diagram to be drawn.** One shot travelling from the player through the
window and controller to the board and back.

## 2.5 Initial class model

**PENDING — diagram to be drawn.** This must show the model as planned, not the
final code; the differences are in section 4.4.
