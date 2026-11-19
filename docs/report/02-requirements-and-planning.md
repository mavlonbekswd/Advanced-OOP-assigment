# 2. Requirements and Planning

*Target: 400 words plus three UML models. Status: complete.*

---

## 2.1 Requirements taken from the brief

The brief fixes the rules, and these were treated as non-negotiable. A ten by
ten grid hides one Battleship of four squares, two Cruisers of three and three
Destroyers of two: six ships on sixteen of the hundred squares, placed so that
no two overlap and no two touch. A square turns blue if empty and red if it
covers a ship, and the game ends once every ship is sunk.

The brief also sets two thresholds. A basic game, with fixed ship positions and typed
coordinates, cannot score above forty per cent; to pass fifty it lists several
extensions. Two were chosen at this stage: random placement and a mouse-controlled
interface, both of which the Week 4 lecture also names as expected. Difficulty levels
and a two-player mode were deliberately deferred, because a smaller application that
works completely was judged more valuable than a larger unfinished one. Difficulty
levels were in fact added later, after the rest of the game was finished; section 4.4
records that change. Two-player play remains undone.

Two points required a decision. First, the brief says ships must not touch but
not whether meeting at a corner counts; this project treats diagonal contact as
touching, which cannot produce a layout the looser rule would reject. Second,
the Week 3 lecture illustrates the same game with a differently named fleet; the
assessment brief is authoritative, so its composition was followed.

## 2.2 How the work was planned

As the work of one person, development followed a waterfall sequence rather than an
iterative team process, as the Week 5 lecture advises: requirements, then
architecture, then code, then tests of each piece as it was finished.

A planning document was produced before any application code existed, recording the
requirements, separating what the brief demands from what this project chose to add,
listing assumptions needing confirmation and setting out an initial architecture. It
has been preserved unchanged, so the differences between plan and outcome can be
shown honestly in section 4.4.

The work was divided into stages that could each be finished and tested alone: the
model, the generator, the controller, then the interface. Each had to pass its own
checks before the next began, so a fault in the rules could never be mistaken for a
fault in the interface.

## 2.3 Initial use case model

*Figure 1 — `docs/diagrams/figure-B-use-case-model.png`*

The only actor is the human player, who starts a game, selects a target, sees hit or
miss feedback, sees victory and restarts. Placing the fleet at random is not
something the player does; it is included by starting a game and by restarting, which
is why it is drawn inside the system with an include relationship rather than
connected to the actor.

## 2.4 Initial sequence model

*Figure 2 — `docs/diagrams/figure-C-sequence-model.png`*

The diagram follows one shot. The player clicks a square, the window asks the
controller to shoot, and the controller either refuses because the game is over
or passes the coordinate to the board, which answers with one of its four
outcomes. The window then redraws the grid from the controller. Showing the two
alternatives is deliberate: the duplicate-shot rule and the end of the game are
where most of the controller's behaviour lives.

## 2.5 Initial class model

*Figure 3 — `docs/diagrams/figure-A-initial-class-model.png`*

This is the model as it was planned, not the finished code. `Board` composes six
ships, a ship is made of coordinates, a placer fills a board, a controller drives
both and a window drives the controller. The differences between this and what was
actually built are listed in section 4.4.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 2.1 Briefdan olingan talablar

Brief o'yin qoidalarini qat'iy belgilaydi va ular muhokama qilinmaydigan deb qabul
qilindi. O'yinchiga 10x10 to'r ko'rsatiladi; unda to'rt katakli bitta Battleship, uch
katakli ikkita Cruiser va ikki katakli uchta Destroyer yashiringan — ya'ni yuz
katakdan o'n oltitasini egallagan olti kema. Kompyuter ularni shunday joylashtirishi
kerakki, hech ikkitasi ustma-ust tushmasin va hech ikkitasi tegmasin. Tanlangan katak
bo'sh bo'lsa ko'k, kema ustida bo'lsa qizil bo'ladi, va hamma kema cho'kkanda o'yin
tugaydi.

Brief ikkita chegara ham belgilaydi. Kemalar doim bir joyda turadigan va koordinata
qo'lda kiritiladigan oddiy o'yin 40% dan yuqori ball ololmaydi; 50% dan oshish uchun
bir nechta kengaytma sanab o'tilgan. Ulardan ikkitasi tanlandi: tasodifiy
joylashtirish va sichqoncha bilan boshqariladigan interfeys — ikkalasini ham 4-hafta
ma'ruzasi kutilayotgan deb ataydi. Qiyinlik darajalari va ikki o'yinchi rejimi ataylab
keyinga qoldirildi, chunki to'liq ishlaydigan kichikroq ilova tugallanmagan
kattaroqdan qimmatliroq deb baholandi.

Ikkita masalada qaror qabul qilish kerak bo'ldi. Birinchidan, brief kemalar tegmasligi
kerak deydi, lekin burchak bilan uchrashish tegish hisoblanadimi — aytmaydi; bu loyiha
diagonal tegishni ham tegish deb hisoblaydi. Qat'iyroq qoida yumshoqroq qoida rad
etadigan joylashuvni hech qachon yarata olmaydi, shuning uchun u ikkala talqinda ham
xavfsiz, va uni testlash oson. Ikkinchidan, 3-hafta ma'ruzasi xuddi shu o'yinni boshqa
nomdagi flot bilan ko'rsatadi. Topshiriq briefi ustun manba, shuning uchun uning flot
tarkibi tanlandi.

### 2.2 Ish qanday rejalashtirilgan

Ish bir kishi tomonidan bajarilgani uchun, 5-hafta ma'ruzasi maslahat berganidek,
rivojlanish iterativ jamoa jarayoni emas, balki waterfall ketma-ketligida bordi:
talablar, keyin arxitektura, keyin kod, keyin har bir qism tugagach uning testlari.

Hech qanday ilova kodi yozilishidan oldin rejalashtirish hujjati tayyorlandi. Unda
talablar yozilgan, brief talab qilgan narsa bilan loyiha o'zi qo'shgan narsa
ajratilgan, tasdiqlash kerak bo'lgan taxminlar sanab o'tilgan va dastlabki arxitektura
belgilangan. U o'zgarishsiz saqlangan, shuning uchun reja bilan natija orasidagi
farqni 4.4-bo'limda halol ko'rsatish mumkin.

Ish har biri alohida tugatilib, alohida testlanadigan bosqichlarga bo'lindi: model,
generator, kontroller, keyin interfeys. Har biri keyingisidan oldin o'z tekshiruvlaridan
o'tishi shart edi, shunda qoidalardagi xato interfeysdagi xato bilan aralashib
ketmaydi.

### 2.3-2.5 UML modellari

**Hali chizilmagan.** Use case: aktyor — odam o'yinchi; use case'lar — o'yinni boshlash,
nishon tanlash, tegdi/tegmadi natijasini ko'rish, g'alabani ko'rish, qayta boshlash.
Sequence: bitta o'qning o'yinchidan oyna va kontroller orqali taxtaga borib qaytishi.
Class: rejadagi model (yakuniy kod emas; farqlar 4.4-da).
