# Design prototype — not part of the submission

This folder is a sketch, kept separate on purpose. Nothing in `src/` and nothing in
`docs/` has been changed for it, so the submitted game, the report, the diagrams and
the screenshots all still match each other exactly as before.

Run it with:

    ./prototype/run.sh

The existing game is unchanged and still starts with:

    java -cp build battleship.App

## What it is trying out

It drives the **real** `Board` and the **real** `RandomShipPlacer`, so the game being
played is the actual game, not a mock-up with fake data. What is new is the way it is
presented, and two pieces of behaviour that the submitted code does not have yet:

| Idea | How it is done here | What it would mean for the real code |
| --- | --- | --- |
| Coordinate labels A–J and 1–10 | Labels around the grid | View only, no change to the model |
| Squares drawn, not lettered | `CellButton.paintComponent` draws a dot or a cross | Replaces the text on the button, which once rendered as empty boxes. Still a `JButton`, so it still raises an action event |
| Fleet panel with sunk ships | Explained below | Could stay in the view, or become a `fleetStatus()` query on the controller |
| Difficulty: board size and a shot limit | The `Session` class | `GameController` would have to own the shot budget and accept a board size |
| Losing the game | Shot budget runs out | New state: at the moment the game can only be won |

## The fleet panel does not break the hiding of the ships

`Board` never says where its ships are, and that is deliberate. The panel still marks
a ship as sunk, and it works this out through the public API alone: when a shot
answers `SUNK`, the cells reachable from it through hits are collected, and how many
there are is the length of the ship that has just gone down. Because no two ships
touch, that group is exactly one ship. The same argument is what the existing unit
checks use to verify a generated fleet.

So the window learns **that** a ship of two squares has sunk. It never learns where
any ship that is still afloat is sitting.

## Difficulty settings used here

| Setting | Board | Shots |
| --- | --- | --- |
| Easy | 12 × 12 | 110 |
| Normal | 10 × 10 | 60 |
| Hard | 8 × 8 | 30 |

The fleet is always the one the brief fixes: one Battleship, two Cruisers, three
Destroyers. Varying the **number** of ships, which the brief also mentions, would
need a change to `RandomShipPlacer`, which this prototype does not make.

Placement was checked at each of these sizes: two hundred boards were generated at
8, 9, 10 and 12 squares and none failed.

## If any of this is kept

It comes back through the normal route: a design note, then the code written
test-first into `src/`, then the report sections and the class diagram updated to
match — the marking scheme asks for the report and the code to agree.
