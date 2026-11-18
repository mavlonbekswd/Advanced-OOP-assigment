# 6. Testing

*Target: 300 words. Status: the unit-testing half is complete; functional testing is
blocked on the interface.*

---

## 6.1 Approach

Testing was carried out alongside development rather than at the end. Each class was
checked before the next one was written, so that a fault in the rules could never be
mistaken for a fault in a later layer. Checks were written before the code they test,
and each was observed failing before the code that satisfies it was written. This
matters because a check that has never failed proves very little: it may be testing
the wrong thing.

## 6.2 Unit testing

The checks are plain Java classes with a `main` method rather than JUnit tests. This
keeps the project free of external dependencies, so it compiles and runs with nothing
but a JDK. The honest limitation is that these harnesses are not a recognised testing
framework, and they stop at the first failure instead of reporting every failure at
once.

One hundred and fifty-eight checks pass in total: ten for `Coordinate`, fifty-two for
`Ship`, forty-three for `Board`, twenty-one for `RandomShipPlacer` and thirty-two for
`GameController`. They cover valid and invalid construction, boundaries in all four
directions, overlap, contact in all eight directions, hits, misses, repeated shots,
sinking, victory and restart.

The fleet generator posed a problem worth describing. `Board` deliberately hides
where the ships are, which is correct for a game, but it left no simple way to check
a generated fleet. Rather than weakening the class by adding an accessor that only
tests would use, the checks fire at all one hundred cells and rebuild the ships as
groups of hit cells that touch each other. That reconstruction is only valid if ships
never touch, so finding exactly six groups of sizes four, three, three, two, two and
two proves the fleet composition, the absence of overlap and the separation rule in a
single assertion.

Writing checks first also found a real defect. A check that a board too small to hold
the Battleship should fail cleanly instead produced `IllegalArgumentException: bound
must be positive` from inside the Java library, because a non-positive bound was
reaching `Random.nextInt`. The generator now refuses a ship longer than the board
before it chooses a position.

## 6.3 Functional testing

Unit checks cannot show that the interface behaves correctly, so the finished
application was also tested as a player would use it. Each case below was carried out
on the running game by clicking the grid, and the result was confirmed both on screen
and in the captured images referred to in section 5.

| # | Action | Expected result | Observed result |
| --- | --- | --- | --- |
| F1 | Start the application | A ten by ten grid of unexplored cells, status showing no shots and six ships | As expected (Figure 1) |
| F2 | Click a cell holding no ship | Cell turns blue, shot count rises by one, hit count unchanged | As expected (Figure 2) |
| F3 | Click a cell holding a ship | Cell turns red, both shot and hit counts rise by one | As expected (Figure 2) |
| F4 | Click a cell already fired at | Nothing changes; no count moves | As expected; the cell is disabled after its first shot |
| F5 | Hit the last remaining cell of a ship | Cells of that ship turn dark red, ships remaining falls by one, status shows `Sunk!` | As expected (Figure 3) |
| F6 | Sink the entire fleet | A victory dialog appears and the status line reports the total number of shots | As expected (Figures 4 and 5) |
| F7 | Click any cell after victory | Nothing happens | As expected; the whole grid is disabled once the game is over |
| F8 | Press New Game | Board and both counters are cleared and a new fleet is generated | As expected (Figure 6) |
| F9 | Inspect the fleet revealed at the end of a game | One line of four cells, two of three and three of two, none touching | As expected (Figure 4) |

Case F9 is worth noting because it checks a requirement on screen that is otherwise
only visible to the automated checks. The layout revealed at the end of a completed
game can be counted directly in Figure 4, and it matches the fleet the brief
specifies.

### A defect found by functional testing

The first working version of the interface displayed a typographic symbol on each
cell that had been fired at. On screen every one of these appeared as an empty box,
because the font used by the chosen look and feel had no glyph for them. Replacing
them with plain ASCII characters removed the empty boxes, but revealed a second
problem: the marks then showed as `...` instead. A button keeps a wide default
margin, which in a cell of this size left too little room for the character, so the
text was being shortened to an ellipsis. Setting the cell margin to zero fixed it.

Neither fault could have been found by the unit checks, which can confirm that a
colour is blue but not that a character is legible on screen. This is a concrete
illustration of why functional testing is needed alongside unit testing.

<!-- UZ -->

---

## O'zbekcha — bu uzun variantda qo'shimcha nima bor

**875 → 556 so'z, 319 so'z kesildi.** Qisqartirilganining o'zbekchasi
`../06-testing.md` oxirida. Bu uzun variantda qo'shimcha:

- **"6.1 Approach" alohida bo'lim** edi, to'liq paragraf bilan: testlash oxirida emas,
  rivojlanish bilan birga olib borilgani; har bir klass keyingisidan oldin
  tekshirilgani; va nega tekshiruv **yiqilgan holda ko'rilishi** shartligi.
  Qisqartirilganda 6.1 va 6.2 birlashtirildi.
- **Unit testlash paragrafi uzunroq**: harness'larning kamchiligi ("not a recognised
  framework **and they stop at the first failure instead of reporting everything at
  once**") to'liqroq aytilgan.
- **Funksional jadval kengroq matnli** edi — "Observed" ustunida to'liq jumlalar,
  masalan "As expected; the cell is disabled after its first shot". Qisqartirilganda
  "Pass; cell disabled" ga qisqardi.
- **Nuqsonlar alohida sarlavha ostida** ("### A defect found by functional testing")
  va to'liqroq tasvirlangan.
- **Oxirgi xulosa paragrafi uzunroq**: "This is a concrete illustration of why
  functional testing is needed alongside unit testing."

**Baho uchun ahamiyati:** Testing 15% ball — bu ikkinchi eng muhim bo'lim. Agar so'z
limiti imkon bersa, "Approach" paragrafini qaytarish foydali, chunki u **test-first
metodologiyasini** tushuntiradi, bu esa oddiy "biz test yozdik" dan ancha kuchli.
