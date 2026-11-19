# 7. Critical Evaluation

*Target: 330 words. Status: complete.*

---

## 7.1 Does the application meet the requirements?

Every requirement from the brief is met, and three of the four extensions it
lists above fifty per cent work: random placement, a mouse-controlled interface
and difficulty levels. The strongest evidence for the placement rules is Figure
8: when the fleet is destroyed its sixteen squares can be counted on screen, so
a requirement otherwise visible only to automated checks is confirmed by eye.
Figure 11 adds the evidence for random placement, and Figures 13 and 14 show a
state the original game could not reach at all, a game lost because the shots
ran out.

One requirement was interpreted rather than simply met. The brief does not say
whether ships meeting at a corner are touching. This project treats diagonal
contact as touching, the stricter reading, so every layout it produces would
also be legal under the looser one; but it reduces the number of possible
layouts and is an interpretation rather than a fact.

## 7.2 Was a plan made, and was it followed?

A planning document was written before any application code existed and
preserved unchanged. The order of work followed it: model, generator,
controller, interface, each tested before the next began, and a development log
records what was actually done at each stage, including what went wrong. The
plan was not followed blindly: five differences are recorded in section 4.4 with
their reasons.

## 7.3 Limitations

The checks are plain Java classes rather than JUnit tests: no dependencies, but
not a recognised framework, and they stop at the first failure. The window has
no automated coverage; its correctness rests on fifteen functional cases run
through the real interface and on holding no rule of its own, and the two
rendering faults in section 6.2 show the risk in that.

`Board` does not enforce the fleet composition, accepting any ships that fit the
placement rules. This lets tests use a board of one or two ships, but a future
caller could build a wrong fleet unchallenged. Nothing is saved, so closing the
window loses the game, and two-player play, the one remaining extension the
brief lists, has not been attempted.

One cosmetic point is worth admitting: when a ship sinks, only the square that
finished it turns the darker red, because each square keeps the outcome of the
shot that struck it. The fleet list beside the grid makes this clear enough to
play with, but the board alone does not.

## 7.4 Object-oriented concepts

Of the fourteen concepts the brief lists, expecting most but not all, this
project demonstrates twelve; they are listed in Appendix C.

Three are absent by decision rather than oversight. No abstract class is used,
because nowhere do two implementations share partial behaviour. No design
pattern was deliberately introduced: the observer relationship between a button
and its listener comes from Swing. Mutator methods are almost entirely absent,
because `Coordinate` and a ship’s positions are immutable; encapsulation was
taken to mean controlling change, not providing a setter for every field.

## 7.5 What could be improved

Converting the checks to JUnit would come first, since it is the gap most likely
to cost marks while the behaviour checked would not change. After that, re-
marking every square of a ship when it sinks, so a destroyed ship reads as one
shape rather than a run of hits. Saving a game in progress and two-player play
would follow, in that order: the first is a small addition to the controller,
the second is close to a second application.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 7.1 Ilova talablarni bajaradimi?

Briefdagi har bir talab bajarilgan va brief sanagan to'rtta kengaytmadan **uchtasi**
ishlaydi: tasodifiy joylashtirish, sichqoncha bilan boshqariladigan interfeys va
qiyinlik darajalari. Joylashtirish qoidalari uchun eng kuchli dalil — 8-rasm: flot
yo'q qilinganda uning 16 katagi ekranda sanaladi. 11-rasm tasodifiy joylashtirishni
ko'rsatadi, 13 va 14-rasm esa eski o'yinda umuman mavjud bo'lmagan holatni — o'q
tugab yutqazishni — ko'rsatadi.

Bitta talab shunchaki bajarilmay, talqin qilindi. Brief burchakda uchrashgan kemalar
tegishgan hisoblanadimi — aytmaydi. Bu loyiha diagonal tegishni tegish deb hisoblaydi,
ya'ni qat'iyroq o'qish; shuning uchun u yaratgan har qanday joylashuv yumshoqroq
talqinda ham qonuniy bo'ladi. Lekin bu mumkin bo'lgan joylashuvlar sonini kamaytiradi
va fakt emas, talqindir.

### 7.2 Reja tuzilganmi va unga amal qilinganmi?

Rejalashtirish hujjati hech qanday ilova kodi mavjud bo'lishidan oldin yozilgan va
o'zgarishsiz saqlangan. Ish tartibi unga amal qildi: model, generator, kontroller,
interfeys — har biri keyingisidan oldin testlandi; rivojlanish jurnali har bosqichda
nima qilinganini, jumladan nima noto'g'ri ketganini yozib bordi. Rejaga ko'r-ko'rona
amal qilinmadi: to'rtta farq 4.4-bo'limda sabablari bilan qayd etilgan.

### 7.3 Cheklovlar

Tekshiruvlar JUnit emas, oddiy Java klasslari: bog'liqlik yo'q, lekin tan olingan
framework ham emas, va ular birinchi xatoda to'xtaydi. Oynaning o'zida avtomatik
qoplam yo'q; uning to'g'riligi haqiqiy interfeysda bajarilgan o'n beshta funksional
holatga va o'zida hech qanday qoida saqlamasligiga tayanadi — 6.2-dagi ikkita nuqson bu xavfni
ko'rsatadi.

`Board` flot tarkibini majburlamaydi, joylashuv qoidalariga mos har qanday kemani
qabul qiladi. Bu testlarga bir yoki ikki kemali taxta ishlatish imkonini beradi,
lekin kelajakdagi chaqiruvchi noto'g'ri flot qurib qo'yishi mumkin. Yana ikki narsa
qoldi: hech narsa saqlanmaydi, va ikki o'yinchi rejimi (qolgan yagona kengaytma)
qilinmagan. Bitta ko'rinish kamchiligini ham tan olish kerak: kema cho'kkanda faqat
oxirgi tekkan katak to'q qizil bo'ladi, chunki har katak unga tekkan o'qning
natijasini saqlaydi. Yon tarafdagi flot ro'yxati buni tushunarli qiladi, lekin
taxtaning o'zida cho'kkan kema qaysi kataklar ekani ko'rinmaydi.

### 7.4 OOP tushunchalari

Brief sanagan o'n to'rtta tushunchadan (u "ko'pini, lekin hammasini emas" kutadi) bu
loyiha o'n ikkitasini ko'rsatadi; ular 4.2-bo'limda sanalgan.

Uchtasi e'tiborsizlikdan emas, qaror bilan yo'q. Abstract class ishlatilmagan, chunki
bu dizaynda hech qayerda ikkita implementatsiya qisman xulqni baham ko'rmaydi. Ataylab
hech qanday design pattern kiritilmagan: tugma va uning listener'i orasidagi observer
munosabati Swing'dan keladi. Mutator metodlar deyarli umuman yo'q, chunki `Coordinate`
va kemaning pozitsiyalari o'zgarmas; encapsulation har bir maydonga setter berish emas,
o'zgarishni nazorat qilish deb tushunilgan.

### 7.5 Nimani yaxshilash mumkin

Birinchi navbatda tekshiruvlarni JUnit'ga o'tkazish, chunki bu ball yo'qotishi eng
ehtimolli bo'shliq, tekshirilayotgan xulq esa o'zgarmaydi. Keyin — kema cho'kkanda
uning **hamma** kataklarini qayta belgilash, shunda taxtada cho'kkan kema yaxlit
shakl bo'lib ko'rinadi. Undan keyin o'yinni saqlash va ikki o'yinchi rejimi.

**Qo'shimcha izoh (18-noyabr).** 7.1-ga bitta jumla qo'shildi: tasodifiy
joylashtirishning ekrandagi isboti endi 11-rasm — ikkinchi o'yin ham oxirigacha
o'ynalib, flot boshqa kataklarda ekani ko'rsatildi. Bundan oldin bu da'voning
faqat avtomatik testlarda isboti bor edi.

**Qo'shimcha izoh (19-noyabr).** Qiyinlik darajalari qo'shilgandan keyin 7.1, 7.3 va
7.5 yangilandi: endi uchta kengaytma ishlaydi, "qiyinlik keyinga qoldirildi" degan
cheklov olib tashlandi, va uning o'rniga rost cheklov yozildi — cho'kkan kemaning
faqat oxirgi katagi to'q qizil bo'lishi.
