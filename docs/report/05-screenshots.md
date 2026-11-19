# 5. Annotated Screenshots

*Target: 150 words. Status: complete; ten captured figures.*

---

The figures below were captured from the running application by driving it
through its own buttons. The harness gives the fleet generator a seeded random
source, so the same images can be produced again, and plays the way a person
does: firing on every other square until something is hit, then working outwards
from it.

**Figure 5 — A new game.** All hundred squares unexplored, the fleet listed beside
the grid and the counters at nought, seventy shots and sixteen ship squares. Nothing
distinguishes a square holding a ship from water.

**Figure 6 — A game in progress.** Misses are blue and marked `o`, hits are red and
marked `X`. A square already fired at is disabled, so a second click does nothing.
One Destroyer has already gone down and is crossed off in the list.

**Figure 7 — Half the fleet sunk.** Twenty shots in, three ships are crossed off. The
square that completes a sinking turns a darker red and is marked `#`; the earlier
hits on that ship stay the lighter red, because each square records the outcome of
the shot that struck it.

**Figure 8 — Victory.** The fleet is destroyed in fifty-six of the seventy shots. All
sixteen ship squares are now red and can be counted on screen: one line of four, two
of three and three of two, none touching at an edge or a corner. The grey squares are
the ones never fired at, which is what a won game looks like when it is played rather
than swept.

**Figure 9 — The victory message.** A dialog reports the end of the game and the
number of shots.

**Figure 10 — After New Game.** The board and both counters are cleared. The image is
indistinguishable from Figure 5, which is the point: nothing of the finished game
remains and the new fleet is hidden again.

**Figure 11 — The second game, won.** Because a cleared board looks exactly like one
that has never been played, the second game was played out as well. Its fleet lies in
different squares and took fifty-nine shots, which is what shows that the ships
really were placed again rather than reused.

**Figure 12 — The Easy setting.** Choosing a different setting rebuilds the grid. The
board is twelve by twelve, the labels run to L and to 12, and a hundred and forty-four
shots are allowed: one per square, so this setting cannot be lost.

**Figure 13 — Losing on Hard.** The board is eight by eight and only forty shots are
allowed. Here they were fired in order rather than hunted, and they ran out with
three ships still afloat and the bottom three rows never touched.

**Figure 14 — The out-of-shots message.** The dialog names the limit that was reached
and how much of the fleet survived.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Rasmlar ishlayotgan ilovadan, uning o'z tugmalari bosilib olingan. Capture dasturi
generatorga **urug'langan (seeded)** tasodifiy manba beradi, shuning uchun xuddi shu
rasmlarni qayta olish mumkin; ilovaning o'zi urug'lanmagan manbadan foydalanadi.

**Muhim o'zgarish:** endi o'q limiti bor, shuning uchun "hamma katakni ketma-ket
bosish" Normal rejimda g'alaba bermaydi — 70 o'q yetmaydi. Capture dasturi endi
odam kabi o'ynaydi: bir katak oralab o't ochadi, tegsa atrofini qidiradi. Shuning
uchun 8-rasmdagi g'alaba haqiqiy o'yinga o'xshaydi (56 o'q, bir qancha katak umuman
bosilmagan).

- **5-rasm** — yangi o'yin: 100 ta ochilmagan katak, yonida flot ro'yxati.
- **6-rasm** — o'yin davomida: ko'k `o` = tegmadi, qizil `X` = tegdi, bitta Destroyer
  allaqachon cho'kkan va ro'yxatdan o'chirilgan.
- **7-rasm** — 20 o'q, 3 ta kema cho'kkan.
- **8-rasm** — g'alaba: 56/70 o'q, 16 katak sanaladi.
- **9-rasm** — g'alaba oynasi.
- **10-rasm** — New Game'dan keyin: **5-rasm bilan bir xil**, va aynan shu muhim.
- **11-rasm** — ikkinchi o'yin yutildi: flot **boshqa kataklarda**, 59 o'q.
- **12-rasm** — Easy: 12x12 to'r, A–L va 1–12, 144 o'q.
- **13-rasm** — Hard'da yutqazish: 8x8, 40 o'q tugadi, 3 kema tirik qoldi.
- **14-rasm** — "o'q tugadi" oynasi.

**Diqqat (halollik masalasi):** 7-rasm izohida yozilgani rost — kema cho'kkanda
uning **hamma** kataklari to'q qizil bo'lmaydi, faqat oxirgi tekkan katak. Eski
hisobotda "kemaning ikkala katagi to'q qizil bo'ladi" deb yozilgan edi, bu noto'g'ri
edi va tuzatildi. Buni kod bilan ham tuzatish mumkin (cho'kkan kemaning hamma
kataklarini qayta belgilash) — 7.5-bo'limda taklif sifatida yozilgan.
