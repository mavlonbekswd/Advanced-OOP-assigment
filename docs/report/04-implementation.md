# 4. Implementation

*Target: 300 words plus the final class model. Status: complete except the diagram.*

---

## 4.1 How the application is built

Nine classes sit in the four layers described in section 3.

In the model, `Coordinate` is an immutable cell location overriding `equals` and
`hashCode`, without which a repeated shot would not be recognised. `Ship` holds its
type, its cells and the cells hit so far, and its constructor rejects anything that
is not a straight unbroken line of the right length, so a ship cannot exist in an
invalid state. `Board` owns the ships and the shot history and is the sole authority
on placement, rejecting any cell out of bounds or within one row and one column of an
existing ship, which is how the diagonal rule is enforced.

`RandomShipPlacer` builds a fleet, longest ship first, since placing the four-cell
Battleship on an empty board wastes far fewer attempts. Retrying is bounded per ship
and per board, and exhaustion raises an exception rather than looping.
`GameController` starts and restarts games, passes shots to the board and counts
shots and hits separately, and refuses further shots once the fleet is destroyed.

In the interface, `CellStyle` returns the colour and mark for one cell and touches no
component, which lets the required colours be checked with no display. `GameWindow`
gives each cell a listener reporting its own position and redraws the grid from the
controller after every shot.

Two faults appeared only when the finished application was run, both concerning the
mark drawn on a cell rather than its colour; they are described in section 6.2.

## 4.2 Object-oriented concepts

Twelve of the concepts the brief lists are demonstrated, each in a specific place in
the code:

| Concept | Where it appears in this project |
| --- | --- |
| Encapsulation | Every field private, collections copied in and returned unmodifiable, ships never exposed |
| Abstraction | `ShipType` and `ShotResult` name what the program needs without revealing storage |
| Inheritance | `GameWindow` extends `JFrame` |
| Polymorphism | Swing handles listeners and components through their declared types |
| Interfaces | Each cell has an `ActionListener` |
| State | A ship's hits, and the board's shot history |
| Constructors | Used to guarantee validity: an invalid `Ship` cannot be created |
| Overloading | Two `Board` constructors, and two `createBoard` methods |
| Overriding | `equals`, `hashCode` and `toString` on `Coordinate` |
| Architectural pattern | Model-View-Controller across the four packages |
| Collections | A list for positions because order matters, a set for hits because a cell is struck once, a map for history because the duplicate rule is key presence |
| Exceptions | Invalid arguments and an exhausted generator are refused rather than ignored |

Abstract classes, a designed pattern and mutator methods are absent; section 7.4
explains each.

## 4.3 Final class model

*Figure 4 — `docs/diagrams/figure-D-final-class-model.png`*

The finished model has ten classes where the plan had six. Three of the additions
are the enums `ShipType` and `ShotResult` and the appearance class `CellStyle`, each
of which exists for a reason given in section 3; the fourth is `App`, the entry
point. The direction of every dependency still runs inwards, from the window to the
controller to the board, and nothing in the model knows about Swing.

## 4.4 Differences from the initial plan

Four changes were made. The plan left open whether ship types should be subclasses;
one class composed with an enum was used instead, for the reason in section 3. The
plan expected JUnit; plain Java harnesses were used, leaving no dependencies, at the
cost given in section 6.2. The plan did not say where the game-over guard would live;
it went to the controller once the difference between a session fact and a grid rule
became clear. Finally, the plan treated the interface as one class, and it became
two, so the required colours could be tested without opening a window.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 4.1 Ilova qanday qurilgan

To'qqiz klass 3-bo'limda tasvirlangan to'rt qatlamda joylashgan.

Modelda `Coordinate` — `equals` va `hashCode` ni qayta aniqlagan o'zgarmas katak
manzili; busiz takroriy o'q tanib olinmas edi. `Ship` o'z turini, kataklarini va shu
paytgacha tegilgan kataklarni saqlaydi, va uning konstruktori to'g'ri uzunlikdagi
to'g'ri, uzluksiz chiziq bo'lmagan hamma narsani rad etadi — shuning uchun noto'g'ri
holatdagi kema umuman mavjud bo'la olmaydi. `Board` kemalar va o'qlar tarixiga
egalik qiladi va joylashuv bo'yicha yagona hokimiyat: chegaradan tashqaridagi yoki
mavjud kemadan bir qator va bir ustun ichidagi har qanday katakni rad etadi — diagonal
qoida aynan shunday majburlanadi.

`RandomShipPlacer` flotni quradi, eng uzun kemadan boshlab, chunki to'rt katakli
Battleship'ni bo'sh taxtaga qo'yish ancha kam urinish sarflaydi. Qayta urinish har
kema va har taxta uchun cheklangan, va urinishlar tugaganda cheksiz aylanish emas,
istisno chiqariladi. `GameController` o'yinni boshlaydi va qayta boshlaydi, o'qlarni
taxtaga uzatadi, o'q va tegishlarni alohida sanaydi, va flot yo'q qilingach keyingi
o'qlarni rad etadi.

Interfeysda `CellStyle` bitta katakning rangi va belgisini qaytaradi va hech qanday
komponentga tegmaydi — aynan shu talab qilingan ranglarni displeysiz tekshirish
imkonini beradi. `GameWindow` har bir katakka o'z pozitsiyasini xabar qiluvchi listener
beradi va har o'qdan keyin to'rni kontroller holatidan qayta chizadi.

### 4.2 Ishlatilgan OOP tushunchalari

**Encapsulation:** hamma maydon private, kolleksiyalar nusxalanib kiradi va
o'zgartirib bo'lmaydigan holda qaytadi, kemalar hech qachon oshkor qilinmaydi.
**Abstraction:** `ShipType` va `ShotResult` dasturga kerak narsani nomlaydi, qanday
saqlanishini oshkor qilmasdan. **Inheritance:** `GameWindow extends JFrame`.
**Polymorphism:** Swing listener va komponentlarni ularning e'lon qilingan turlari
orqali ishlatadi. **Interfaces:** har katakda `ActionListener`. **State:** kemaning
tegishlari va o'qlar tarixi. **Constructors:** to'g'rilikni kafolatlash uchun.
**Overloading:** `Board` ning ikkita konstruktori. **Overriding:** `equals`,
`hashCode`, `toString`. **Architectural pattern:** MVC. **Kolleksiya tanlovi:**
pozitsiyalar uchun list, chunki tartib muhim; tegishlar uchun set, chunki katakka bir
marta tegiladi; tarix uchun map, chunki takror qoidasi kalit bor-yo'qligi masalasi.

Abstract class, ataylab qo'yilgan design pattern va mutator metodlar yo'q; har biri
7.4-bo'limda izohlangan.

### 4.3 Yakuniy class model

**Hali chizilmagan**, to'rt qatlamni qamrashi kerak.

### 4.4 Dastlabki rejadan farqlar

To'rtta o'zgarish qilindi. Reja kema turlari subclass bo'ladimi degan savolni ochiq
qoldirgan edi; buning o'rniga enum bilan birikkan bitta klass ishlatildi (sabab
3-bo'limda). Reja JUnit kutgan edi; o'rniga oddiy Java harness'lar ishlatildi, bu
hech qanday tashqi bog'liqlik qoldirmadi — narxi 6.2-da. Reja o'yin tugaganini
tekshiruvchi qulf qayerda turishini aytmagan edi; u kontrollerga ketdi, sessiya fakti
bilan to'r qoidasi orasidagi farq aniq bo'lgach. Nihoyat, reja interfeysni bitta klass
deb hisoblagan edi, u ikkita bo'ldi — shunda talab qilingan ranglarni oyna ochmasdan
testlash mumkin bo'ldi.
