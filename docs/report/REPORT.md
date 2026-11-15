# Battleship development report — working draft

Module: MOD004883. Author: to complete. Submission date: to verify in Canvas.

## Introduction
The project aims to implement a single-player Java Battleship game with a testable domain model and a mouse-controlled interface.

## Planning
See preserved initial design, requirements, architecture, proposed UML and roadmap. The original document predates this repository setup; importing it does not establish a Git creation date for that planning work.

## Implementation
Implemented so far: immutable Coordinate, ShipType, Ship with validated positions and hit tracking, Board with placement validation and shot processing, and a demonstration entry point. Random placement, controller and Swing interface are not implemented. Describe changes from the original plan as development occurs.

## Testing
The starter includes ten dependency-free Coordinate checks and 52 Ship checks, executed successfully on 14 November 2026. These are not JUnit tests. Record actual runs in the testing log. Full game functional evidence remains pending.

## Critical evaluation
Current limitation: this is a starter, not a playable game. Evaluate the completed game against each requirement later using actual evidence.

## Interview appendix
Pending the lecturer's original ten questions. Student must write and understand their own answers.

## Submission preparation
Pending: final class UML; annotated real screenshots; test evidence; sources; cover page; contents; figures list; captions; final Word formatting. Do not submit this scaffold as a completed report.

### Board milestone — 15 November 2026
Board delegates individual hit tracking to Ship and owns placement, adjacency and shot history. It copies ships to prevent outside mutations. The model has no Swing dependency. All 105 self-checks passed on Java 21 (10 Coordinate, 52 Ship, 43 Board). These harnesses remain separate from the planned JUnit and real GUI testing. See testing/BOARD-RESULTS.md for limitations.
