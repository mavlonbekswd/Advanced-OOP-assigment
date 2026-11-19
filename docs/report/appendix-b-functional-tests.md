# Appendix B — Functional Test Cases

*These are the cases discussed in section 6.2. They were run against the finished
application through its own buttons.*

---

| # | Action | Expected | Observed |
| --- | --- | --- | --- |
| F1 | Start the application | 100 unexplored squares, no shots, six ships listed | Pass, Fig. 5 |
| F2 | Click an empty square | Blue; shots +1, hits unchanged | Pass, Fig. 6 |
| F3 | Click a ship square | Red; shots +1, hits +1 | Pass, Fig. 6 |
| F4 | Click a square already fired at | Nothing changes | Pass; square disabled |
| F5 | Hit a ship's last square | Dark red, the ship crossed off, `Sunk` rises | Pass, Figs. 6, 7 |
| F6 | Sink the whole fleet | Victory dialog and total shots | Pass, Figs. 8, 9 |
| F7 | Click after victory | Nothing happens | Pass; grid disabled |
| F8 | Press New Game | Board and counters cleared, new fleet | Pass, Fig. 10 |
| F9 | Count the revealed fleet | One 4, two 3s, three 2s, none touching | Pass, Fig. 8 |
| F10 | Win a second game | A different fleet in different squares | Pass, Fig. 11 |
| F11 | Choose Easy | Grid rebuilt 12 by 12, labels to L and 12, 144 shots | Pass, Fig. 12 |
| F12 | Choose Hard | Grid rebuilt 8 by 8, 40 shots | Pass; 64 squares, `0 / 40` |
| F13 | Use every shot on Hard | Out-of-shots dialog, grid disabled, ships still afloat | Pass, Figs. 13, 14 |
| F14 | Change the setting two shots into a game | Asks first; declining leaves the game untouched | Pass; still Hard, `2 / 40`, status line unchanged |
| F15 | Press Rules | Explains the fleet, the colours and the shot limit | Pass |

F10 was added after the first set of screenshots was reviewed. Figure 10, taken
straight after New Game, is indistinguishable from Figure 5, so by itself it cannot
show that a new fleet was placed; only playing the second game out makes the new
positions visible.

F11 to F15 were added with the difficulty setting. F14 matters most of the five: the
setting is the one control that can destroy a game in progress by accident, so the
window has to ask before it does, and put the box back afterwards if the answer is
no.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Bu jadval ilgari 6.2-bo'limda edi; so'z chegarasi uchun Appendix B ga ko'chirildi,
o'chirilmadi — u 15% testlash bahosi uchun kuchli dalil.

**F10 keyin qo'shildi.** Sababi: New Game'dan keyingi rasm (10-rasm) 5-rasm bilan
aynan bir xil bo'lib chiqdi, ya'ni u "yangi flot joylashtirildi" degan da'voni
isbotlay olmaydi. Shuning uchun ikkinchi o'yin ham oxirigacha o'ynalib, floti
boshqa kataklarda ekani ko'rsatildi (11-rasm).

**F11–F15 qiyinlik darajasi bilan birga qo'shildi** va hammasi haqiqiy oynada
bajarildi, natijalari yozib olindi:

- F11: Easy → 144 katak, `0 / 144`
- F12: Hard → 64 katak, `0 / 40`
- F13: 40 o'q tugadi, 3 kema tirik (13, 14-rasm)
- F14: o'yin o'rtasida darajani almashtirsa **so'raydi**; "No" desa o'yin o'zgarmaydi
  (hamon Hard, `2 / 40`) va ro'yxat eski darajaga qaytadi
- F15: Qoidalar oynasi ochiladi
