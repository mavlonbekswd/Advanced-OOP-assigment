# Testing plan and evidence

| Area | Planned checks | Current result |
|---|---|---|
| Coordinate | getters, boundary policy, equality/hash, string representation | Starter harness available |
| Ship | hit tracking, repeated hit, sunk only at full length | Not implemented |
| Board | invalid placement, bounds, overlap, all eight touching neighbours, hit/miss, repeat shot | Not implemented |
| Random placement | seeded repeatable fleet checks, exact counts and lengths, retry exhaustion | Not implemented |
| Controller | victory, post-game input, restart | Not implemented |
| GUI | mouse input, colours, messages, restart on real display | Not implemented |

Add actual run date, command, outcome and environment. Automated model checks do not prove GUI behaviour. Add JUnit coverage before claiming completion of the unit-testing milestone.

## 2026-11-03 setup verification
Compilation attempted in this environment, but `javac` is not installed. Tests were not executed here. Earlier reported results are not a fresh verification.
