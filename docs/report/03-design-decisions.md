# 3. Design Decisions and Justification

*Target: 250 words. Status: complete.*

---

The application is divided into four layers: a model holding the state and the
rules, a service generating a fleet, a controller running the lifecycle of a game,
and an interface drawing it. This follows the Model-View-Controller pattern covered
in the Week 6 lecture, and was chosen for a practical reason rather than to demonstrate a
pattern: the rules can be tested completely without opening a window.

The first decision was whether the three kinds of ship should be subclasses. They
were not: the brief distinguishes them only by length, and a subclass adding no
behaviour its parent lacks adds complexity without meaning. A single `Ship` class
holds a `ShipType` enum recording the length instead. The Week 3 lecture reaches the
same conclusion about this game, noting a ship hierarchy is not really necessary for
an example this simple though it would help in a more complex one.

The second concerns where each rule lives. Only `Board` decides whether a placement
is legal. The fleet generator chooses positions at random but never judges them; it
offers each candidate to the board and accepts the verdict. The rule about ships not
touching therefore appears in exactly one method and cannot be stated inconsistently
in two places, which is the DRY principle from the Week 7 lecture.

The third concerns what each class refuses to reveal. `Board` never hands out its
ships and the controller offers no route to the board, so the interface receives the
board size, the outcome of every cell already fired at, the counts and whether the
game is over: enough to draw the grid and announce victory, not enough to expose
where the ships are.

Finally, the cross-platform look and feel is requested at start-up, because some
native ones ignore a button's background colour and the brief requires blue and red
cells. That makes it a requirement rather than a matter of taste.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

Ilova to'rt qatlamga bo'lingan: holat va qoidalarni saqlovchi model, flot yaratuvchi
servis, o'yin hayot tsiklini boshqaruvchi kontroller va uni chizuvchi interfeys. Bu
6-hafta ma'ruzasida ko'rsatilgan Model-View-Controller andozasiga amal qiladi, va u andozani
ko'rsatish uchun emas, amaliy sabab bilan tanlangan: qoidalarni oyna ochmasdan to'liq
testlash mumkin.

Birinchi qaror — uch xil kema alohida subclass bo'lishi kerakmi degan savol edi.
Bo'lmadi: brief ularni faqat uzunligi bilan farqlaydi, va ota-klassida yo'q hech
qanday xulq qo'shmaydigan subclass ma'nosiz murakkablik keltiradi. Buning o'rniga
bitta `Ship` klassi uzunlikni saqlovchi `ShipType` enum'ini ushlab turadi. 3-hafta
ma'ruzasi aynan shu o'yin haqida shunday xulosaga keladi: bunchalik oddiy misol uchun
kema ierarxiyasi "haqiqatan ham shart emas", garchi murakkabroq o'yinda foydali
bo'lardi.

Ikkinchi qaror — har bir qoida qayerda yashashi haqida. Faqat `Board` joylashuv
qonuniymi yoki yo'qligini hal qiladi. Flot generatori pozitsiyalarni tasodifiy tanlaydi,
lekin ularni hech qachon baholamaydi; har bir nomzodni taxtaga taklif qiladi va uning
hukmini qabul qiladi. Shuning uchun "kemalar tegmasligi" qoidasi aynan bitta metodda
turadi va ikki joyda bir-biriga zid yozilishi mumkin emas — bu 7-hafta ma'ruzasidagi DRY
prinsipi.

Uchinchi qaror — har bir klass nimani oshkor qilishdan bosh tortishi haqida. `Board`
o'z kemalarini hech qachon tashqariga bermaydi, kontroller esa taxtaga yo'l ochmaydi;
shuning uchun interfeys faqat taxta o'lchamini, o'q uzilgan har bir katakning
natijasini, hisoblagichlarni va o'yin tugaganini oladi — to'rni chizish va g'alabani
e'lon qilish uchun yetarli, kemalar qayerdaligini oshkor qilish uchun yetarli emas.

Nihoyat, ishga tushganda cross-platform look and feel so'raladi, chunki ba'zi tizim
look and feel'lari tugmaning fon rangini e'tiborsiz qoldiradi, brief esa ko'k va qizil
kataklarni talab qiladi. Bu uni did masalasi emas, brief talabi qiladi.
