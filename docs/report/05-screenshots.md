# 5. Annotated Screenshots

*Target: 150 words. Status: complete; seven captured figures.*

---

The figures below were captured from the running application by driving it through
its own buttons. The capture harness supplies the fleet generator with a seeded
random source, so the same images can be produced again; the application itself uses
an unseeded one.

**Figure 5 — A new game.** All hundred cells unexplored; the status line reads no
shots, no hits, six ships. Nothing distinguishes a cell holding a ship from water.

**Figure 6 — A game in progress.** Misses are blue and marked `o`, hits are red and
marked `X`. Cells already fired at are disabled, so a second click does nothing.

**Figure 7 — A ship sunk.** The Destroyer's two cells turn a darker red, marked `#`,
and the status line falls to four ships and shows `Sunk!`. The brief requires hits to
be red; the darker shade is an addition separating a completed sinking from a ship
still afloat.

**Figure 8 — Victory.** The fleet is destroyed in eighty-nine shots and its sixteen
cells are now visible. The shapes can be counted on screen: one line of four, two of
three and three of two, none touching at an edge or a corner.

**Figure 9 — The victory message.** A dialog reports the end of the game and the
number of shots.

**Figure 10 — After Restart.** New Game clears the board and both counters. The image
is identical to Figure 5, which is the point: nothing of the finished game remains,
and the new fleet is hidden again.

**Figure 11 — The second game, won.** Because a cleared board looks exactly like one
that has never been played, the second game was also played to the end. Its fleet
occupies different cells from Figure 8 and took ninety-two shots, which is what shows
that the ships really were placed again rather than reused.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Rasmlar ishlayotgan ilovadan, uning o'z tugmalari bosilib olingan. Capture dasturi
generatorga **urug'langan (seeded)** tasodifiy manba beradi, shuning uchun xuddi shu
rasmlarni qayta olish mumkin; ilovaning o'zi urug'lanmagan manbadan foydalanadi — bu
halol, chunki aks holda "tasodifiy" degan da'vo tekshirib bo'lmaydigan bo'lardi.

- **5-rasm** — yangi o'yin: 100 ta ochilmagan katak, kema qayerdaligi bilinmaydi.
- **6-rasm** — o'yin davomida: ko'k `o` = tegmadi, qizil `X` = tegdi.
- **7-rasm** — kema cho'kdi: to'q qizil `#`, status `Sunk!`.
- **8-rasm** — g'alaba: 16 katak ochiladi, flot tarkibi ekranda sanaladi.
- **9-rasm** — g'alaba oynasi.
- **10-rasm** — Restart'dan keyin: **5-rasm bilan bir xil**, va aynan shu muhim —
  taxta butunlay tozalangan.
- **11-rasm** — ikkinchi o'yin yutildi: flot **boshqa kataklarda**, 92 o'q.

**Diqqat (muhim o'zgarish):** avval 10-rasm "yangi flot yangi joylarda" deb yozilgan
edi, lekin rasmda buni ko'rib bo'lmaydi — tozalangan taxta hech qachon o'ynalmagan
taxtaga o'xshaydi. Shuning uchun 11-rasm qo'shildi: ikkinchi o'yin ham oxirigacha
o'ynaldi va floti boshqa joyda ekani ko'rsatildi. Endi "tasodifiy joylashtirish"
da'vosining ekranda isboti bor.
