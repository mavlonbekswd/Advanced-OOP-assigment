# 7. Critical Evaluation

*Target: 330 words. Status: complete.*

---

## 7.1 Does the application meet the requirements?

Every requirement from the brief is met, and both chosen extensions work. The
strongest evidence for the placement rules is Figure 8: when a game ends the sixteen
ship cells become visible and the fleet can be counted on screen, so a requirement
otherwise visible only to automated checks can be confirmed by eye. Figure 11 adds
the evidence for random placement: a second game, played to its end, hides the fleet
in different cells.

One requirement was interpreted rather than simply met. The brief does not say
whether ships meeting at a corner are touching. This project treats diagonal contact
as touching, the stricter reading, so every layout it produces would also be legal
under the looser one; but it reduces the number of possible layouts and is an
interpretation rather than a fact.

## 7.2 Was a plan made, and was it followed?

A planning document was written before any application code existed and preserved
unchanged. The order of work followed it: model, generator, controller, interface,
each tested before the next began, and a development log records what was actually
done at each stage, including what went wrong. The plan was not followed blindly:
four differences are recorded in section 4.4 with their reasons.

## 7.3 Limitations

The checks are plain Java classes rather than JUnit tests: no dependencies, but not
a recognised framework, and they stop at the first failure. The window itself has no
automated coverage; its correctness rests on ten hand-run functional cases and on
holding no rule of its own, and the two rendering faults in section 6.2 show the risk
in that.

`Board` does not enforce the fleet composition, accepting any ships that fit the
placement rules. This lets tests use a board of one or two ships, but a future caller
could build a wrong fleet unchallenged. Finally, nothing is saved, and the difficulty
levels and two-player mode the brief offers were deliberately deferred.

## 7.4 Object-oriented concepts

Of the fourteen concepts the brief lists, expecting most but not all, this project
demonstrates twelve; they are listed in section 4.2.

Three are absent by decision rather than oversight. No abstract class is used,
because nowhere do two implementations share partial behaviour. No design pattern was
deliberately introduced: the observer relationship between a button and its listener
comes from Swing. Mutator methods are almost entirely absent, because `Coordinate`
and a ship's positions are immutable; encapsulation was taken to mean controlling
change, not providing a setter for every field.

## 7.5 What could be improved

Converting the checks to JUnit would come first, since it is the gap most likely to
cost marks while the behaviour checked would not change. After that, naming the type
of ship when one is sunk, and adding difficulty levels through the board-size
parameter that already exists and is already tested.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 7.1 Ilova talablarni bajaradimi?

Briefdagi har bir talab bajarilgan va ikkala tanlangan kengaytma ham ishlaydi.
Joylashtirish qoidalari uchun eng kuchli dalil — 4-rasm: o'yin tugaganda o'n olti
kema katagi ko'rinadi va flotni ekranda sanash mumkin, ya'ni boshqa holda faqat
avtomatik tekshiruvlarga ko'rinadigan talab ko'z bilan tasdiqlanadi.

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
qoplam yo'q; uning to'g'riligi qo'lda bajarilgan o'nta funksional holatga va o'zida
hech qanday qoida saqlamasligiga tayanadi — 6.2-dagi ikkita nuqson bu xavfni
ko'rsatadi.

`Board` flot tarkibini majburlamaydi, joylashuv qoidalariga mos har qanday kemani
qabul qiladi. Bu testlarga bir yoki ikki kemali taxta ishlatish imkonini beradi,
lekin kelajakdagi chaqiruvchi noto'g'ri flot qurib qo'yishi mumkin. Nihoyat, hech
narsa saqlanmaydi, va brief taklif qilgan qiyinlik darajalari hamda ikki o'yinchi
rejimi ataylab keyinga qoldirilgan.

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
ehtimolli bo'shliq, tekshirilayotgan xulq esa o'zgarmaydi. Keyin, kema cho'kkanda
uning turini xabarda aytish, va allaqachon mavjud va testlangan taxta o'lchami
parametri orqali qiyinlik darajalarini qo'shish.

**Qo'shimcha izoh (18-noyabr).** 7.1-ga bitta jumla qo'shildi: tasodifiy
joylashtirishning ekrandagi isboti endi 11-rasm — ikkinchi o'yin ham oxirigacha
o'ynalib, flot boshqa kataklarda ekani ko'rsatildi. Bundan oldin bu da'voning
faqat avtomatik testlarda isboti bor edi.
