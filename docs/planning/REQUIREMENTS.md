# Requirements baseline

Source: supplied `For-Assesment-task-Only.txt`, Part A and Part B. Lecture claims in the imported architecture notes have not been independently re-audited in this repository setup.

| ID | Requirement | Status |
|---|---|---|
| FR01 | 10 × 10 hidden board | Model complete; no interface yet |
| FR02 | 1 Battleship length 4, 2 Cruisers length 3, 3 Destroyers length 2 | Complete and tested in RandomShipPlacer |
| FR03 | No overlap or touching | Complete and tested in Board, enforced for generated fleets |
| FR04 | Blue miss, red hit | Outcomes modelled as ShotResult; colours need the GUI |
| FR05 | End when all ships sink | Complete in the controller; the on-screen message needs the GUI |
| MVP01 | Random placement | Selected enhancement, implemented 18 Nov 2026 |
| MVP02 | Mouse-controlled Swing GUI | Selected enhancement |
| MVP03 | Duplicate shots do not change counters; restart clears state | Implemented and tested 18 Nov 2026 |

Assumption: touching includes diagonals. Clarify with lecturer if necessary. Difficulty and two-player play are deferred. No database, networking or accounts.

Report: planning before implementation, initial use-case/sequence/class UML, implementation differences, final class UML, annotated screenshots, functional and unit testing, critical evaluation, ten actual tutorial interview questions, cover/contents/figures/introduction/captions. Interview question source is still missing. Never invent answers to unseen questions.

Mark distribution in supplied brief: code 20%; planning 25%, implementation 10%, testing 15%, evaluation 15%, interview 10%, report quality 5%.
