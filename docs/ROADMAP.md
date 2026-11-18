# Zero to MVP roadmap

P0 to P4 are implemented. The game is playable; P5 verification and P6 submission remain. This is a roadmap, not a claim of completed development.

| Phase | Deliverable | Exit criteria | Status |
|---|---|---|---|
| P0 | Requirements and initial design | Preserve original planning baseline and identify assumptions | Baseline imported |
| P1 | Domain model | Coordinate, Ship, Board; bounds, shots, sunk state tested | Complete (self-check harnesses; JUnit pending) |
| P2 | Fleet placement | Exact fleet, both orientations, no overlap/touching, bounded retries | Complete (21 checks; seeded repeatability and a 1,000-fleet stress run) |
| P3 | Controller | Start/restart, reject duplicate shots, victory state | Complete (32 checks) |
| P4 | Swing GUI | 100 clickable cells, red/blue feedback, restart, victory message | Complete (12 style checks, 9 functional cases, 6 screenshots) |
| P5 | MVP verification | Unit tests plus real GUI functional checks, Javadoc, reproducible run | Planned |
| P6 | Report and submission | Evidence, final UML, evaluation, interview appendix, Word export and Java ZIP | Planned |

Keep main runnable. Make small commits for actual work. Do not manufacture a historical development sequence. Record design changes as they occur. Add optional features only after P5 passes.
