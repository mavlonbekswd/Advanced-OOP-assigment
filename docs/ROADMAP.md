# Zero to MVP roadmap

P0 to P4 are implemented. The game is playable; P5 verification and P6 submission remain. This is a roadmap, not a claim of completed development.

| Phase | Deliverable | Exit criteria | Status |
|---|---|---|---|
| P0 | Requirements and initial design | Preserve original planning baseline and identify assumptions | Baseline imported |
| P1 | Domain model | Coordinate, Ship, Board; bounds, shots, sunk state tested | Complete (self-check harnesses; JUnit pending) |
| P2 | Fleet placement | Exact fleet, both orientations, no overlap/touching, bounded retries | Complete (21 checks; seeded repeatability and a 1,000-fleet stress run) |
| P3 | Controller | Start/restart, reject duplicate shots, victory state | Complete (120 checks, including defeat on the shot limit) |
| P4 | Swing GUI | 100 clickable cells, red/blue feedback, restart, victory message | Complete (12 style checks, 15 functional cases, 10 screenshots) |
| P5 | MVP verification | Unit tests plus real GUI functional checks, Javadoc, reproducible run | Complete (296 checks, 0 compiler and 0 javadoc warnings) |
| P6 | Report and submission | Evidence, final UML, evaluation, interview appendix, Word export and Java ZIP | Word document assembled; interview answers and cover details outstanding |
| P7 | Difficulty extension | Board size and shot limit per setting, a game that can be lost | Complete (26 Difficulty checks; added after P5, see the development log) |

Keep main runnable. Make small commits for actual work. Do not manufacture a historical development sequence. Record design changes as they occur. Add optional features only after P5 passes.
