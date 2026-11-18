# Battleship development report — working draft

Module MOD004883, Component 2. Author: to complete. Submission date: to verify in
Canvas.

**This is a draft, not a submission.** Sections marked PENDING have not been written,
and sections marked BLOCKED cannot be written until something outside this repository
is obtained. Nothing here should be submitted as finished work.

The report is written in Markdown, one file per section, and is assembled into the
required Word document at the end. Everything is written in the third person, as the
report is a description of the project rather than a diary.

**Each section file ends with an Uzbek block** after an `<!-- UZ -->` marker, so the
content can be checked in Uzbek before it goes into the Word document. Those blocks
are for review only: they are not part of the report and `wordcount.sh` stops
counting when it reaches the marker.

**`long/` holds the earlier 4,476-word draft**, kept as an archive. The Uzbek block in
each of those files explains what that longer version contains that the submitted one
does not, and whether it is worth restoring if the word limit allows.

## Sections

| # | File | Marks | Target words | Status |
|---|---|---|---|---|
| 1 | [Introduction](01-introduction.md) | part of 5% | 120 | Complete |
| 2 | [Requirements and planning](02-requirements-and-planning.md) | 25% | 450 | Prose complete; three UML diagrams pending |
| 3 | [Design decisions](03-design-decisions.md) | part of 25% | 250 | Complete |
| 4 | [Implementation](04-implementation.md) | 10% | 300 | Complete; final UML diagram pending |
| 5 | [Screenshots](05-screenshots.md) | part of 10% | 100 | Complete; six captured figures |
| 6 | [Testing](06-testing.md) | 15% | 300 | Complete; unit and functional |
| 7 | [Critical evaluation](07-critical-evaluation.md) | 15% | 300 | Complete |
| 8 | [Conclusion](08-conclusion.md) | part of 5% | 80 | Complete |
| 9 | [References](09-references.md) | part of 5% | — | Pending formatting |
| A | [Interview questions](appendix-a-interview-questions.md) | 10% | ~600 | Questions obtained; **answers must be written by the student** |

Run `./wordcount.sh` from this folder to see the current length of each section.

## Word limit — verified

The Week 4 lecture states it directly: the component is **3,000 words in total**,
with **about 1,500–2,000 words for the report**, and the report "can be up to 3,000
words". The figure recorded in the planning document is therefore confirmed.

Whether captions, tables, references and the appendix count towards the limit is
still not stated in any available source, and should be confirmed with the module
leader.

### Current length

| Section | Words |
| --- | --- |
| 1 Introduction | 124 |
| 2 Requirements and planning | 451 |
| 3 Design decisions | 312 |
| 4 Implementation | 440 |
| 5 Screenshots | 210 |
| 6 Testing | 556 |
| 7 Critical evaluation | 428 |
| 8 Conclusion | 75 |
| **Main body** | **2,596** |
| Appendix A (questions only; answers still to be written) | 613 |

The draft has been cut from about 4,480 words to 2,596. That is inside the stated
maximum of 3,000 but above the recommended 2,000.

**Reaching 2,000 would mean removing content that carries marks**, so it is a
decision rather than an edit. The options, in the order they cost least:

1. Move the nine-row functional test table to the appendix (about 130 words). The
   table is strong evidence for the 15% testing mark, so it should be moved rather
   than deleted.
2. Reduce the figure captions in section 5 to one line each (about 80 words).
3. Move the list of object-oriented concepts in section 4.2 into a two-column table
   (about 100 words, and it reads better as a table).
4. Cut the restatement of the requirements in section 2.1 to a short list (about 140
   words). This is the riskiest, since planning carries 25%.

Together those come to roughly 450 words, which lands the main body near 2,150.
Going below that would start removing argument rather than wording.

## Still to obtain

1. The exact submission deadline. Only "end of Week 12" is stated in the Week 1
   slides.
2. Whether captions, tables, references and the appendix count towards the word
   limit. The limit itself is now confirmed.
3. Whether "two ships do not touch" is intended to include diagonal contact. This
   project assumes it does and says so explicitly.

## Final deliverables

A formatted MS Word document and a separate ZIP of the Java code, submitted through
Canvas. The Word document needs a cover page, a table of contents, a table of
figures, numbered sections, captions on every figure and a single consistent font.

## Progress note — 18 November 2026

The application is complete and playable, which unblocks the three sections that were
waiting on it. Sections 5 and 6 are now written from real evidence: six screenshots of
the running game and nine functional test cases, including two rendering defects that
the automated checks could not have found.

Sections 7 and 8 can now be written, and the interview appendix remains the only part
blocked by something outside this repository.

## Progress note — 18 November 2026, later

Sections 7 and 8 are written, so every part of the report now exists except the
interview appendix. What remains is mechanical rather than creative: the trimming
pass described above, the four UML diagrams, formatting the references, and
assembling the Word document.
