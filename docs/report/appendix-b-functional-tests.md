# Appendix B — Functional Test Cases

*These are the cases discussed in section 6.2. They were run by hand on the finished
application.*

---

| # | Action | Expected | Observed |
| --- | --- | --- | --- |
| F1 | Start the application | 100 unexplored cells, no shots, six ships | Pass, Fig. 5 |
| F2 | Click an empty cell | Blue; shots +1, hits unchanged | Pass, Fig. 6 |
| F3 | Click a ship cell | Red; shots +1, hits +1 | Pass, Fig. 6 |
| F4 | Click a cell already fired at | Nothing changes | Pass; cell disabled |
| F5 | Hit a ship's last cell | Dark red, ships left falls, `Sunk!` | Pass, Fig. 7 |
| F6 | Sink the whole fleet | Victory dialog and total shots | Pass, Figs. 8, 9 |
| F7 | Click after victory | Nothing happens | Pass; grid disabled |
| F8 | Press New Game | Board and counters cleared, new fleet | Pass, Fig. 10 |
| F9 | Count the revealed fleet | One 4, two 3s, three 2s, none touching | Pass, Fig. 8 |
| F10 | Win a second game | A different fleet in different cells | Pass, Fig. 11 |

F10 was added after the first set of screenshots was reviewed. Figure 10, taken
straight after Restart, is identical to Figure 5, so by itself it cannot show that a
new fleet was placed; only playing the second game to its end makes the new positions
visible.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Bu jadval ilgari 6.2-bo'limda edi; so'z chegarasi uchun Appendix B ga ko'chirildi,
o'chirilmadi — u 15% testlash bahosi uchun kuchli dalil.

**F10 keyin qo'shildi.** Sababi: Restart'dan keyingi rasm (10-rasm) 5-rasm bilan
aynan bir xil bo'lib chiqdi, ya'ni u "yangi flot joylashtirildi" degan da'voni
isbotlay olmaydi. Shuning uchun ikkinchi o'yin ham oxirigacha o'ynalib, floti
boshqa kataklarda ekani ko'rsatildi (11-rasm).
