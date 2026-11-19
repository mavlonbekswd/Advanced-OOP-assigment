# 4. Implementation

*Target: 300 words plus the final class model. Status: complete.*

---

## 4.1 How the application is built

Fourteen classes, counting the entry point, sit in the four layers described in
section 3.

In the model, `Coordinate` is an immutable square location overriding `equals`
and `hashCode`, without which a repeated shot would not be recognised. `Ship`
holds its type, its squares and the squares hit so far, and its constructor
rejects anything that is not a straight unbroken line of the right length, so a
ship cannot exist in an invalid state. `Board` owns the ships and the shot
history and is the sole authority on placement, rejecting any square out of
bounds or within one row and one column of an existing ship, which is how the
diagonal rule is enforced. `Difficulty` pairs a board size with a shot limit,
and `ShipStatus` records one ship’s type and condition so the fleet can be
listed without its positions leaving `Board`.

`RandomShipPlacer` builds a fleet, longest ship first, since placing the four-
square Battleship on an empty board wastes far fewer attempts. Retrying is
bounded, and exhaustion raises an exception rather than looping.
`GameController` starts games at a chosen setting, passes shots to the board,
counts shots and hits, and decides whether a game has been won, lost on the shot
limit, or neither.

The interface is four classes. `CellStyle` returns the colour and mark for one
square and touches no component, which lets the required colours be checked with
no display. `BoardPanel` draws the labelled grid and reports a click through a
listener interface of its own. `FleetPanel` lists the enemy ships and the
counts. `GameWindow` assembles them, holds the toolbar and the status line, and
redraws both panels after every shot.

Two faults appeared only when the finished application was run, both concerning
the mark drawn on a square rather than its colour; they are described in section
6.2.

## 4.2 Object-oriented concepts

Twelve of the concepts the brief lists are demonstrated. Appendix C ties each
one to the place in the code where it appears; the three that are absent are
explained in section 7.4.

## 4.3 Final class model

*Figure 4 — `docs/diagrams/figure-D-final-class-model.png`*

The finished model has fourteen classes where the plan had six. Four are the
enums `ShipType`, `ShotResult` and `Difficulty` and the value object
`ShipStatus`; three more are `CellStyle` and the two panels the window is built
from; the last is `App`, the entry point. Every dependency still runs inwards,
from the window to the controller to the board, and nothing in the model knows
about Swing.

## 4.4 Differences from the initial plan

Five changes were made. The plan left open whether ship types should be
subclasses; one class composed with an enum was used instead, for the reason in
section 3. The plan expected JUnit; plain Java harnesses were used, leaving no
dependencies, at the cost given in section 6.2. The plan did not say where the
game-over guard would live; it went to the controller once the difference
between a session fact and a grid rule became clear. The plan also treated the
interface as one class, and it became four.

The fifth change is the one the plan argued against. Difficulty levels were
listed as deferred in section 2.1, and were added once the rest of the game was
finished and its checks were passing, because the cost turned out to be small:
`Board` already took its size as a constructor argument and that argument was
already tested. Deferring it was still the right decision at the time, since the
work that made it cheap had not been done yet.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 4.1 Ilova qanday qurilgan

O'n to'rt klass (kirish nuqtasi bilan birga) 3-bo'limda tasvirlangan to'rt qatlamda
joylashgan.

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

Tayyor modelda 14 klass bor, rejada 6 ta edi. Qo'shilganlar: `ShipType`, `ShotResult`,
`Difficulty` enum'lari, `ShipStatus` qiymat obyekti, `CellStyle` va oyna qurilgan ikkita
panel, hamda `App`. Bog'liqliklar yo'nalishi hamon ichkariga qarab boradi va modelda
Swing haqida hech narsa yo'q.

### 4.4 Dastlabki rejadan farqlar

Beshta o'zgarish qilindi. Reja kema turlari subclass bo'ladimi degan savolni ochiq
qoldirgan edi; buning o'rniga enum bilan birikkan bitta klass ishlatildi (sabab
3-bo'limda). Reja JUnit kutgan edi; o'rniga oddiy Java harness'lar ishlatildi, bu
hech qanday tashqi bog'liqlik qoldirmadi — narxi 6.2-da. Reja o'yin tugaganini
tekshiruvchi qulf qayerda turishini aytmagan edi; u kontrollerga ketdi, sessiya fakti
bilan to'r qoidasi orasidagi farq aniq bo'lgach. Reja interfeysni bitta klass deb hisoblagan edi, u to'rtta
bo'ldi. Beshinchi o'zgarish — rejaga zid bo'lgani: 2.1-da "keyinga qoldirildi" deb
yozilgan qiyinlik darajalari qolgan hammasi tugab, testlari o'tgandan keyin qo'shildi.
Qaror o'zgardi, chunki narxi arzon chiqdi: `Board` allaqachon o'lchamni konstruktor
orqali olardi va bu allaqachon testlangan edi. Lekin o'sha paytda uni keyinga qoldirish
hamon to'g'ri qaror edi — uni arzon qilgan ish hali bajarilmagan edi.
