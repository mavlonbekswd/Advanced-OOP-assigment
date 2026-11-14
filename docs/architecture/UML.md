# Proposed UML — not the final implementation

## Class model

```mermaid
classDiagram
class Coordinate {
  -int row
  -int column
  +getRow() int
  +getColumn() int
  +isWithin(int) boolean
}
class Ship {
  +isSunk() boolean
}
class Board {
  +fireAt(Coordinate)
  +allShipsSunk() boolean
}
class RandomShipPlacer
class GameController
class GameWindow
Board "1" *-- "6" Ship
Ship --> Coordinate
RandomShipPlacer --> Board
GameController --> Board
GameController --> RandomShipPlacer
GameWindow --> GameController
```

Only Coordinate exists. Other signatures remain proposals. Ship inheritance/factory decisions require a useful behavioural reason; different lengths alone do not justify a subclass hierarchy.

## Shot sequence

```mermaid
sequenceDiagram
actor Player
participant View as GameWindow
participant Controller as GameController
participant Model as Board
Player->>View: Click cell
View->>Controller: shoot(coordinate)
Controller->>Model: fireAt(coordinate)
Model-->>Controller: Result and game state
Controller-->>View: Display state
View-->>Player: Colour and victory feedback
```

## Use cases

Actor: human player. Use cases: start game; select target; inspect hit/miss feedback; see victory; restart. Random placement occurs as part of starting a game. This list is a draft inventory: draw the formal UML use-case diagram for the planning report before further implementation.
