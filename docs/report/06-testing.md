# 6. Testing

*Target: 400 words. Status: complete.*

---

## 6.1 Unit testing

Testing ran alongside development: each class was checked before the next was
written, and each check was watched failing before the code that satisfies it
was written, because a check that has never failed may be testing the wrong
thing. The checks are plain Java classes with a `main` method rather than JUnit
tests, which leaves the project free of dependencies at the cost of not being a
recognised framework.

Three hundred and sixteen checks pass: ten for `Coordinate`, fifty-two for
`Ship`, fifty-five for `Board`, twenty-six for `Difficulty`, twenty-one for
`RandomShipPlacer`, one hundred and twenty for `GameController` and thirty-two
for `CellStyle`, covering valid and invalid construction, all four boundaries,
overlap, contact in all eight directions, hits, misses, repeated shots, sinking,
victory, defeat and restart. The mark colour is measured against each cell
colour and must clear a three-to-one contrast ratio.

The generator posed a problem worth describing. `Board` hides where the ships
are, correctly for a game, but that left no simple way to check a generated
fleet. Rather than weaken the class with an accessor only tests would use, the
checks fire at every square and rebuild the ships as groups of touching hits.
Finding six groups of sizes four, three, three, two, two and two then proves
composition, absence of overlap and the separation rule at once.

Whether a game can really be lost cannot be settled by one example, so the
controller is played out on the Hard setting against ten different seeds; the
checks confirm that each game ends, is won or lost and never both, and never
exceeds its limit. The list of `ShipStatus` objects `Board` hands out is checked
the way a careless caller might treat it: an old copy is kept, another shot
fired, and the copy confirmed unchanged.

Writing checks first also found a defect: a board too small for the Battleship
produced `IllegalArgumentException: bound must be positive` from inside the Java
library, because a non-positive bound reached `Random.nextInt`. The generator
now refuses a ship longer than the board first.

## 6.2 Functional testing

Unit checks cannot show the interface behaves correctly, so the finished game
was also worked as a player would. The fifteen cases run are listed in Appendix
B; five of them cover the difficulty setting, including the one case where the
interface has to protect the player from itself, by asking before a change of
setting throws away a game in progress.

Two defects were found this way, both invisible to every automated check. The
first interface drew a typographic symbol on each fired-at square and every one
appeared as an empty box, because the look and feel’s font had no glyph for
them. Switching to plain ASCII revealed a second fault: the marks then showed as
three dots, because a Swing button’s wide default margin left too little room
for one character. Setting the margin to zero fixed it. Unit checks can confirm
a colour is blue but not that a character is legible.

<!-- UZ -->

---

## O'zbekcha (tekshirish uchun — hisobotga kirmaydi)

### 6.1 Unit testlash

Testlash rivojlanish bilan birga bordi: har bir klass keyingisi yozilishidan oldin
tekshirildi, va har bir tekshiruv uni qanoatlantiradigan kod yozilishidan **oldin**
yiqilgan holda ko'rildi — chunki hech qachon yiqilmagan tekshiruv noto'g'ri narsani
tekshirayotgan bo'lishi mumkin. Tekshiruvlar JUnit emas, `main` metodli oddiy Java
klasslari; bu loyihani tashqi bog'liqliklardan xoli qoldiradi, lekin ular tan olingan
framework emas.

Uch yuz o'n uch tekshiruv o'tadi: `Coordinate` uchun 10, `Ship` uchun 52,
`Board` uchun 55, `Difficulty` uchun 26, `RandomShipPlacer` uchun 21,
`GameController` uchun 120 va `CellStyle` uchun 29 —
to'g'ri va noto'g'ri qurish, to'rt chegara, ustma-ustlik, sakkiz yo'nalishdagi tegish,
tegishlar, o'tkazib yuborishlar, takroriy o'qlar, cho'kish, g'alaba va qayta
boshlashni qamraydi.

Generator alohida muammo tug'dirdi. `Board` kemalar qayerdaligini yashiradi — o'yin
uchun to'g'ri, lekin bu yaratilgan flotni tekshirishning oddiy yo'lini qoldirmadi.
Klassni faqat testga kerak bo'ladigan getter bilan zaiflashtirish o'rniga,
tekshiruvlar yuz katakning hammasiga o't ochadi va kemalarni bir-biriga tegib turgan
tegish kataklari guruhlari sifatida qayta tiklaydi. Bu faqat kemalar tegmasa
to'g'ri — shuning uchun o'lchamlari 4, 3, 3, 2, 2, 2 bo'lgan oltita guruh topilishi
flot tarkibini, ustma-ustlik yo'qligini va ajralish qoidasini bir vaqtda isbotlaydi.

Tekshiruvlarni oldin yozish haqiqiy nuqson ham topdi: Battleship sig'maydigan kichik
taxta Java kutubxonasi ichidan `IllegalArgumentException: bound must be positive`
chiqardi, chunki `Random.nextInt` ga musbat bo'lmagan chegara borgan edi. Endi
generator taxtadan uzun kemani avval rad etadi.

### 6.2 Funksional testlash

Unit tekshiruvlar interfeys to'g'ri ishlashini ko'rsata olmaydi, shuning uchun tayyor
o'yin o'yinchi kabi ham testlandi. O'n beshta holat (F1–F15) Appendix B da:
ilovani ishga tushirish, bo'sh katakni bosish, kema katagini bosish, allaqachon o'q
uzilgan katakni bosish, kemaning oxirgi katagiga tegish, butun flotni cho'ktirish,
g'alabadan keyin bosish, New Game bosish va oxirida ochilgan flotni sanash.
Hammasi muvaffaqiyatli o'tdi.

F9 avtomatik tekshiruvlardan boshqa holda ko'rinmaydigan talabni ekranda tasdiqlaydi.
F11–F15 qiyinlik darajasini qamraydi; eng muhimi F14 — o'yin o'rtasida darajani
almashtirmoqchi bo'lsangiz oyna avval so'raydi, "yo'q" desangiz o'yin o'zgarmaydi.

Shu yo'l bilan ikkita nuqson topildi, ikkalasi ham hamma avtomatik tekshiruvga
ko'rinmas edi. Birinchi interfeys har bir o'q uzilgan katakka tipografik belgi
chizardi va ularning hammasi bo'sh kvadrat bo'lib ko'rindi, chunki tanlangan look and
feel shriftida u belgilar uchun glif yo'q edi. Oddiy ASCII'ga o'tish ikkinchi
nuqsonni ochdi: belgilar `...` bo'lib ko'rindi, chunki Swing tugmasining keng
standart margin'i bitta harfga ham joy qoldirmagan edi. Katak margin'ini nolga
qo'yish buni tuzatdi. Unit tekshiruv rang ko'k ekanini tasdiqlay oladi, lekin harf
o'qilishini emas — ikkala tur test ham shuning uchun kerak bo'ldi.

**19-noyabr qo'shimchasi.** Belgilar o'zgardi: `o X #` o'rniga nuqta, xoch va to'ldirilgan
kvadrat. Eski nuqson (shrift belgini chizolmay bo'sh kvadrat chiqarishi) qaytmasligi
uchun `CellStyle.marksFor(Font)` avval shriftdan **uchalasini ham chiza olasanmi** deb
so'raydi; chizolmasa eski `o X #` ga qaytadi. `CellStyle` tekshiruvlari 12 dan 29 ga
chiqdi — ikkala to'plam ham, qaytish yo'li ham testlangan.
