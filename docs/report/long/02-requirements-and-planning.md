# 2. Requirements and Planning

*Target: 450 words of prose plus three UML models. Status: prose complete; the three
diagrams still have to be drawn.*

---

## 2.1 Requirements taken from the brief

The assessment brief fixes the rules of the game, and these were treated as
non-negotiable. The player is shown a ten by ten grid that hides one Battleship of
four cells, two Cruisers of three cells each and three Destroyers of two cells each.
That is six ships occupying sixteen of the hundred cells. The computer must place
them so that no two ships overlap and no two ships touch. When the player selects a
square it turns blue if it is empty and red if it covers part of a ship, and the game
ends once every ship has been sunk.

The brief also states that a basic game, in which the ships always sit in the same
place and the player types coordinates, cannot score above forty per cent. To go
beyond fifty per cent it lists several possible extensions. Two of these were chosen
as the target for this project: random ship placement and a full graphical interface
controlled with the mouse. Difficulty levels and a two-player mode were considered
and deliberately deferred, because a smaller application that works completely was
judged more valuable than a larger one that is unfinished.

One point in the brief is genuinely ambiguous. It says that two ships must not touch,
but it does not say whether two ships that meet only at a corner count as touching.
This project treats diagonal contact as touching, which is the stricter of the two
readings. The stricter rule was chosen because it cannot produce a layout that the
looser rule would reject, so it is safe under either interpretation, and because it
is simple to state and therefore simple to test.

## 2.2 How the work was planned

Because the project is the work of one person, the development followed a waterfall
sequence rather than an iterative team process. Requirements were written first, then
the architecture, then the code, then the tests of each piece as it was completed.

A planning document was produced before any application code was written. It records
the requirements above, separates what the brief demands from what this project chose
to add, lists the assumptions that needed checking, and sets out an initial
architecture. That document has been preserved unchanged as a baseline so that the
differences between the original plan and the finished application can be shown
honestly later in this report.

The work was then broken into stages that could each be finished and tested on their
own: the domain model, the fleet generator, the game controller and finally the user
interface. Each stage had to pass its own checks before the next one began, so that a
fault in the rules could never be mistaken for a fault in the interface.

## 2.3 Initial use case model

**PENDING — diagram must be drawn.**

The draft inventory of use cases is recorded in `docs/architecture/UML.md`: the single
actor is the human player, and the use cases are start a game, select a target,
inspect hit or miss feedback, see victory, and restart. Random placement happens as
part of starting a game.

## 2.4 Initial sequence model

**PENDING — diagram must be drawn.**

A draft in Mermaid form exists in `docs/architecture/UML.md`, covering a single shot
travelling from the player through the window and controller to the board and back.

## 2.5 Initial class model

**PENDING — diagram must be drawn.**

A draft exists in `docs/architecture/UML.md`. It must be presented as the model as it
was planned, not as the final code, because the two differ; the differences are
described in section 4.

<!-- UZ -->

---

## O'zbekcha — bu uzun variantda qo'shimcha nima bor

**585 → 451 so'z, ya'ni 134 so'z kesildi.** Qisqartirilgan variantning o'zbekchasi
`../02-requirements-and-planning.md` oxirida. Bu uzun variantda qo'shimcha quyidagilar
bor:

- **Briefning ikki chegarasi haqida to'liqroq izoh.** Uzun variantda alohida
  tushuntirilgan: "oddiy o'yin 40% dan oshmaydi" va "50% dan oshish uchun brief bir
  nechta kengaytmani sanaydi". Qisqartirilganda bu bitta jumlaga siqilgan.
- **Qiyinlik darajalari va ikki o'yinchi rejimini nega qoldirgani** to'liqroq
  asoslangan: "to'liq ishlaydigan kichikroq ilova tugallanmagan kattaroqdan
  qimmatliroq deb baholandi" — bu fikr qisqartirilganda ham saqlangan, lekin qisqaroq.
- **Diagonal talqini alohida paragraf** sifatida berilgan va "oddiy aytiladi, shuning
  uchun oddiy testlanadi" degan qo'shimcha sabab bilan. Qisqartirilganda bu
  birlashtirilgan.
- **Week 3 ziddiyati alohida sarlavhali paragraf** ("A second conflict was found
  between sources") sifatida yozilgan, Frigate/Cruiser nomlari aniq aytilgan.
  Qisqartirilganda bir jumla bo'lib qolgan.
- **UML bo'limlari (2.3–2.5) batafsilroq**: har biri `docs/architecture/UML.md` ga
  havola qiladi va draft qayerdaligini aytadi.

**Baho uchun ahamiyati:** Planning 25% ball. Agar so'z limiti imkon bersa, bu
bo'limning uzun variantini tiklash mantiqiy — ayniqsa Week 3 ziddiyati va diagonal
talqini paragraflarini, chunki ular manbalar bilan ishlaganingizni ko'rsatadi.
