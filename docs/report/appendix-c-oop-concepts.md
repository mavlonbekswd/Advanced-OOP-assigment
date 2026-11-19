# Appendix C — Object-Oriented Concepts in This Project

*These are the concepts discussed in sections 4.2 and 7.4. Twelve of the fourteen the
brief lists are demonstrated; the three absent ones are explained in section 7.4.*

---

| Concept | Where it appears in this project |
| --- | --- |
| Encapsulation | Every field private, collections copied in and returned unmodifiable, ships never exposed even when the fleet is listed |
| Abstraction | `ShipType`, `ShotResult` and `Difficulty` name what the program needs without revealing storage |
| Inheritance | `GameWindow` extends `JFrame` |
| Polymorphism | Swing handles listeners and components through their declared types |
| Interfaces | `BoardPanel` declares its own `ShotListener` so the grid can report a click without knowing what one means; each square also has an `ActionListener` |
| State | A ship's hits, and the board's shot history |
| Constructors | Used to guarantee validity: an invalid `Ship` cannot be created |
| Overloading | Two `Board` constructors, two `createBoard` methods and two `newGame` methods |
| Overriding | `equals`, `hashCode` and `toString` on `Coordinate`; `toString` on `Difficulty` so a combo box needs no renderer |
| Architectural pattern | Model-View-Controller across the four packages |
| Collections | A list for positions because order matters, a set for hits because a square is struck once, a map for history because the duplicate rule is key presence |
| Exceptions | Invalid arguments, a shot at a finished game and an exhausted generator are refused rather than ignored |

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Bu jadval ilgari 4.2-bo'limda edi. Qiyinlik darajasi qo'shilgandan keyin hisobot
3,614 so'zga chiqib ketdi, chegara esa 3,000. Jadval o'chirilmadi — Appendix C ga
ko'chirildi, xuddi funksional testlar jadvali Appendix B ga ko'chirilganidek.
Ilovalar so'z chegarasiga kirmaydi deb hisoblanmoqda (buni modul rahbaridan
tasdiqlatish kerak — REPORT.md dagi ochiq savollar ro'yxatiga kiritilgan).

4.2-bo'limda endi bitta jumla qoldi va Appendix C ga yo'naltiradi.
