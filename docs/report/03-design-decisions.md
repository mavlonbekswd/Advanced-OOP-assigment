# 3. Design Decisions and Justification

*Target: 250 words. Status: complete.*

---

The application is divided into four layers: a model holding the state and the
rules, a service generating a fleet, a controller running the lifecycle of a
game, and an interface drawing it. This follows the Model-View-Controller
pattern shown in the Week 6 lecture, and was chosen for a practical reason: the
rules can be tested completely without opening a window.

The first decision was whether the three kinds of ship should be subclasses.
They were not: the brief distinguishes them only by length, and a subclass
adding no behaviour its parent lacks adds complexity without meaning. A single
`Ship` class holds a `ShipType` enum recording the length instead, and the Week
3 lecture reaches the same conclusion about this game.

The second concerns where each rule lives. Only `Board` decides whether a
placement is legal. The fleet generator chooses positions at random but never
judges them; it offers each candidate to the board and accepts the verdict. The
rule about ships not touching therefore appears in exactly one method and cannot
be stated inconsistently in two places, which is the DRY principle from the Week
7 lecture.

The third concerns what each class refuses to reveal. `Board` never hands out
its ships and the controller offers no route to the board, so the interface
receives the board size, the outcome of every square already fired at, the
counts and whether the game is over: enough to draw the grid, not enough to
expose where the ships are.

The difficulty setting, added after the rest of the game was finished, raised
both questions again. A setting changes the size of the grid and the number of
shots and nothing else, since the brief fixes the fleet. Both figures belong in
the model, as a `Difficulty` enum, but the shot limit is enforced by the
controller, because the number of shots allowed is a fact about one session of
play while the board knows only squares, ships and shots. Listing the enemy
fleet beside the grid tested the third decision directly: the interface must
report which ships have gone down without learning where they are, so `Board`
reduces each ship to a `ShipStatus`, its type and whether it has sunk, with no
positions at all.

Finally, the cross-platform look and feel is requested at start-up, because some
native ones ignore a button’s background colour and the brief requires blue and
red squares. That makes it a requirement rather than a matter of taste.

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

Qiyinlik darajasi (o'yin tugagandan keyin qo'shilgan) xuddi shu savolni yana ko'tardi.
Daraja faqat to'r o'lchamini va ruxsat etilgan o'q sonini o'zgartiradi, boshqa hech
narsani — flotni brief belgilab qo'ygan. Ikkala son modelda, `Difficulty` enum'ida
turadi; lekin o'q chegarasini `Board` emas, kontroller sanaydi va majburlaydi, chunki
o'yinchiga nechta o'q berilishi — bitta o'yin seansiga tegishli fakt, taxta esa faqat
kataklar, kemalar va nima otilganini biladi.

Yon tarafdagi "Enemy fleet" ro'yxati uchinchi qarorni bevosita sinovdan o'tkazdi.
Interfeys qaysi kema cho'kkanini aytishi kerak, lekin ularning qayerdaligini bilmasligi
shart. Shuning uchun `Board` har bir kemani `ShipStatus` ga qisqartiradi: turi va
cho'kkanmi — pozitsiya umuman yo'q. Ya'ni yashirin kemalarni oshkor qilishni talab
qilgandek ko'ringan funksiya ularni oshkor qilmasdan qurildi.

Nihoyat, ishga tushganda cross-platform look and feel so'raladi, chunki ba'zi tizim
look and feel'lari tugmaning fon rangini e'tiborsiz qoldiradi, brief esa ko'k va qizil
kataklarni talab qiladi. Bu uni did masalasi emas, brief talabi qiladi.
