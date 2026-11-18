# Battleship development report — working draft

Module MOD004883, Component 2. Author: to complete. Submission date: to verify in
Canvas.

**This is a draft, not a submission.** Sections marked PENDING have not been written,
and sections marked BLOCKED cannot be written until something outside this repository
is obtained. Nothing here should be submitted as finished work.

The report is written in Markdown, one file per section, and is assembled into the
required Word document at the end. Everything is written in the third person, as the
report is a description of the project rather than a diary.

## Sections

| # | File | Marks | Target words | Status |
|---|---|---|---|---|
| 1 | [Introduction](01-introduction.md) | part of 5% | 120 | Complete |
| 2 | [Requirements and planning](02-requirements-and-planning.md) | 25% | 450 | Prose complete; three UML diagrams pending |
| 3 | [Design decisions](03-design-decisions.md) | part of 25% | 250 | Complete |
| 4 | [Implementation](04-implementation.md) | 10% | 300 | Complete; final UML diagram pending |
| 5 | [Screenshots](05-screenshots.md) | part of 10% | 100 | Complete; six captured figures |
| 6 | [Testing](06-testing.md) | 15% | 300 | Complete; unit and functional |
| 7 | [Critical evaluation](07-critical-evaluation.md) | 15% | 300 | **Pending** — now unblocked |
| 8 | [Conclusion](08-conclusion.md) | part of 5% | 80 | **Pending** — write last |
| 9 | [References](09-references.md) | part of 5% | — | Pending |
| A | [Interview questions](appendix-a-interview-questions.md) | 10% | ~800 | **Blocked** — the official question list is missing |

Run `./wordcount.sh` from this folder to see the current length of each section.

## Word limit

A recommended length of 1,500–2,000 words with a maximum of 3,000 is recorded in the
preserved planning document, which attributes it to the Week 4 slides. **Those slides
are not among the available materials, so this figure has not been verified.**
Whether appendices, references, captions and tables count towards the limit is not
stated in any source available to this project. Both points should be confirmed with
the module leader.

The section targets above add up to roughly 1,900 words excluding the appendix, which
sits inside the recommended range if the appendix is excluded from it.

**The drafted sections run well over their targets and a trimming pass is now
required.** As of 18 November the written sections total about 3,900 words against a
combined target of 1,900. Writing long first was deliberate, because it is easier to
cut an argument that has been made in full than to expand a thin one, but the gap is
now large.

The three worst offenders are section 4 at 949 words against a target of 300,
section 6 at 875 against 300, and section 2 at 577 against 450. Section 6 is harder
to cut than it looks, since the testing section carries 15% of the marks and the two
rendering defects it records are strong evidence; section 4 is the right place to
start.

Run `./wordcount.sh` after each pass. Note that the limit itself is still unverified,
so the target may change.

## Still to obtain

1. The exact submission deadline. Only "end of Week 12" is stated in the Week 1
   slides.
2. Confirmation of the word limit and what counts towards it.
3. The Word document containing the ten interview questions.
4. Whether "two ships do not touch" is intended to include diagonal contact. This
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
