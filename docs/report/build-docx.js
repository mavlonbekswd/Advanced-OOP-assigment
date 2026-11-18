/*
 * Builds the submission Word document from the report sections.
 *
 * The prose here is kept identical to the Markdown files in this folder; this
 * script adds only what Word has to supply: a cover page, a table of contents,
 * a table of figures, numbered headings, the ten captioned figures and a single
 * font throughout.
 *
 * Usage, from the repository root:
 *   NODE_PATH=<folder holding node_modules> node docs/report/build-docx.js
 *
 * The word count printed on the cover page is passed in as the first argument
 * so that it always comes from wordcount.sh rather than from this file.
 */

const fs = require('fs');
const path = require('path');
const {
  AlignmentType, Document, Footer, HeadingLevel, ImageRun, LevelFormat,
  PageBreak, PageNumber, Packer, Paragraph, SectionType, SequentialIdentifier,
  ShadingType, Table, TableCell, TableRow, TableOfContents, TextRun, WidthType,
} = require('docx');

const BODY_FONT = 'Arial';
const CODE_FONT = 'Courier New';
const ROOT = path.resolve(__dirname, '..', '..');
const WORDS = process.argv[2] || 'see wordcount.sh';

/* ---------- inline formatting -------------------------------------------- */

// Splits a line on **bold** and `code` and returns the runs for it.
function runs(text, base = {}) {
  const out = [];
  const re = /(\*\*[^*]+\*\*|`[^`]+`)/g;
  let last = 0;
  let m;
  while ((m = re.exec(text)) !== null) {
    if (m.index > last) {
      out.push(new TextRun({ text: text.slice(last, m.index), ...base }));
    }
    const token = m[0];
    if (token.startsWith('**')) {
      out.push(new TextRun({ text: token.slice(2, -2), bold: true, ...base }));
    } else {
      out.push(new TextRun({ text: token.slice(1, -1), font: CODE_FONT, size: 20, ...base }));
    }
    last = m.index + token.length;
  }
  if (last < text.length) {
    out.push(new TextRun({ text: text.slice(last), ...base }));
  }
  return out;
}

function p(text, opts = {}) {
  return new Paragraph({
    children: runs(text),
    alignment: AlignmentType.JUSTIFIED,
    spacing: { after: 160, line: 276 },
    ...opts,
  });
}

function h1(text) {
  return new Paragraph({ text, heading: HeadingLevel.HEADING_1 });
}

function h2(text) {
  return new Paragraph({ text, heading: HeadingLevel.HEADING_2 });
}

function bullet(text) {
  return new Paragraph({
    children: runs(text),
    numbering: { reference: 'report-bullets', level: 0 },
    spacing: { after: 100, line: 276 },
  });
}

/* ---------- figures ------------------------------------------------------ */

// A figure is the picture, then a caption numbered by a SEQ field so that the
// table of figures can collect it.
function figure(file, width, height, caption) {
  return [
    new Paragraph({
      alignment: AlignmentType.CENTER,
      spacing: { before: 160, after: 80 },
      children: [new ImageRun({
        type: 'png',
        data: fs.readFileSync(path.join(ROOT, file)),
        transformation: { width, height },
      })],
    }),
    new Paragraph({
      alignment: AlignmentType.CENTER,
      style: 'Caption',
      spacing: { after: 240 },
      children: [
        new TextRun({ text: 'Figure ' }),
        new SequentialIdentifier('Figure'),
        new TextRun({ text: ' — ' + caption }),
      ],
    }),
  ];
}

/* ---------- tables ------------------------------------------------------- */

function cell(text, { bold = false, width, shaded = false } = {}) {
  return new TableCell({
    width: { size: width, type: WidthType.DXA },
    shading: shaded ? { type: ShadingType.CLEAR, fill: 'DCEAFB' } : undefined,
    margins: { top: 60, bottom: 60, left: 100, right: 100 },
    children: [new Paragraph({
      spacing: { after: 0, line: 240 },
      children: runs(text, bold ? { bold: true } : {}),
    })],
  });
}

function table(columnWidths, rows) {
  return new Table({
    columnWidths,
    width: { size: columnWidths.reduce((a, b) => a + b, 0), type: WidthType.DXA },
    rows: rows.map((cells, i) => new TableRow({
      tableHeader: i === 0,
      children: cells.map((text, j) => cell(text, {
        width: columnWidths[j], bold: i === 0, shaded: i === 0,
      })),
    })),
  });
}

/* ---------- cover page --------------------------------------------------- */

function coverLine(text, opts = {}) {
  return new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { after: 120 },
    children: [new TextRun({ text, ...opts })],
  });
}

const cover = [
  new Paragraph({ text: '', spacing: { after: 1600 } }),
  coverLine('A Single-Player Battleship Game in Java', { bold: true, size: 44 }),
  coverLine('Design, Implementation and Testing', { size: 28 }),
  new Paragraph({ text: '', spacing: { after: 800 } }),
  coverLine('MOD004883 Advanced Object-Oriented Programming', { size: 24 }),
  coverLine('Element 010, Component 2: Development Report', { size: 24 }),
  new Paragraph({ text: '', spacing: { after: 800 } }),
  coverLine('Student name: [to complete]', { size: 24 }),
  coverLine('Student ID (SID): [to complete]', { size: 24 }),
  coverLine('Anglia Ruskin University', { size: 24 }),
  new Paragraph({ text: '', spacing: { after: 800 } }),
  coverLine('Word count, sections 1 to 8: ' + WORDS, { size: 22 }),
  coverLine('(excluding figure captions, references and appendices)', { size: 20, italics: true }),
  coverLine('Submitted: [to complete]', { size: 22 }),
  new Paragraph({ children: [new PageBreak()] }),
];

/* ---------- front matter ------------------------------------------------- */

// The two front-matter titles are not Heading 1, or they would list themselves
// inside the table of contents.
function frontTitle(text) {
  return new Paragraph({
    spacing: { before: 0, after: 240 },
    children: [new TextRun({ text, font: BODY_FONT, size: 30, bold: true })],
  });
}

const frontMatter = [
  frontTitle('Table of Contents'),
  new TableOfContents('Contents', { hyperlink: true, headingStyleRange: '1-2' }),
  new Paragraph({ children: [new PageBreak()] }),
  frontTitle('Table of Figures'),
  new TableOfContents('Figures', { hyperlink: true, captionLabel: 'Figure' }),
  new Paragraph({ children: [new PageBreak()] }),
];

/* ---------- the report --------------------------------------------------- */

const body = [];

body.push(h1('1. Introduction'));
body.push(p('This report describes the planning, design, implementation and testing of a single-player Battleship game written in Java for module MOD004883. In the game, a human player fires at a ten by ten grid that hides a computer-placed fleet of six ships, and the game ends when every ship has been sunk.'));
body.push(p('The report is organised to follow the order in which the work was carried out. It begins with the requirements taken from the assessment brief and the plan that was written before any code existed, then explains the main design decisions and how they were put into practice. It then presents the testing that was used to show the rules behave correctly, and finishes by evaluating the finished application against the original requirements.'));

body.push(h1('2. Requirements and Planning'));
body.push(h2('2.1 Requirements taken from the brief'));
body.push(p('The brief fixes the rules, and these were treated as non-negotiable. The player is shown a ten by ten grid hiding one Battleship of four cells, two Cruisers of three and three Destroyers of two: six ships occupying sixteen of the hundred cells. The computer must place them so that no two overlap and no two touch. A selected square turns blue if empty and red if it covers a ship, and the game ends once every ship is sunk.'));
body.push(p('The brief also sets two thresholds. A basic game, with fixed ship positions and typed coordinates, cannot score above forty per cent; to pass fifty it lists several extensions. Two were chosen: random placement and a mouse-controlled interface, both of which the Week 4 lecture also names as expected. Difficulty levels and a two-player mode were deliberately deferred, because a smaller application that works completely was judged more valuable than a larger unfinished one.'));
body.push(p('Two points required a decision. First, the brief says ships must not touch but not whether meeting at a corner counts; this project treats diagonal contact as touching. The stricter rule cannot produce a layout the looser rule would reject, so it is safe under either reading and is simple to test. Second, the Week 3 lecture illustrates the same game with a differently named fleet. The assessment brief is authoritative, so its composition was followed.'));

body.push(h2('2.2 How the work was planned'));
body.push(p('As the work of one person, development followed a waterfall sequence rather than an iterative team process, as the Week 5 lecture advises: requirements, then architecture, then code, then tests of each piece as it was finished.'));
body.push(p('A planning document was produced before any application code existed, recording the requirements, separating what the brief demands from what this project chose to add, listing assumptions needing confirmation and setting out an initial architecture. It has been preserved unchanged, so the differences between plan and outcome can be shown honestly in section 4.4.'));
body.push(p('The work was divided into stages that could each be finished and tested alone: the model, the generator, the controller, then the interface. Each had to pass its own checks before the next began, so a fault in the rules could never be mistaken for a fault in the interface.'));

body.push(h2('2.3 Initial use case model'));
body.push(p('The only actor is the human player, who starts a game, selects a target, sees hit or miss feedback, sees victory and restarts. Placing the fleet at random is not something the player does; it is included by starting a game and by restarting, which is why it is drawn inside the system with an include relationship rather than connected to the actor.'));
body.push(...figure('docs/diagrams/figure-B-use-case-model.png', 340, 416, 'Initial use case model. The player is the only actor; random placement is included by starting and restarting a game.'));

body.push(h2('2.4 Initial sequence model'));
body.push(p('The diagram follows one shot. The player clicks a cell, the window asks the controller to shoot, and the controller either refuses because the game is already won or passes the coordinate to the board. The board answers with one of its four outcomes, consulting a ship only when the cell has not been fired at before. The window then asks the controller for the state of every cell and redraws the grid. Showing the two alternatives on the diagram is deliberate: the duplicate-shot rule and the end of the game are where most of the controller’s behaviour lives.'));
body.push(...figure('docs/diagrams/figure-C-sequence-model.png', 330, 494, 'Initial sequence model for a single shot, including the duplicate-shot and game-over alternatives.'));

body.push(h2('2.5 Initial class model'));
body.push(p('This is the model as it was planned, not the finished code. `Board` composes six ships, a ship is made of coordinates, a placer fills a board, a controller drives both and a window drives the controller. The differences between this and what was actually built are listed in section 4.4.'));
body.push(...figure('docs/diagrams/figure-A-initial-class-model.png', 560, 336, 'Initial class model, as planned before any code was written.'));

body.push(h1('3. Design Decisions and Justification'));
body.push(p('The application is divided into four layers: a model holding the state and the rules, a service generating a fleet, a controller running the lifecycle of a game, and an interface drawing it. This follows the Model-View-Controller pattern shown in the Week 6 lecture, and was chosen for a practical reason rather than to demonstrate a pattern: the rules can be tested completely without opening a window.'));
body.push(p('The first decision was whether the three kinds of ship should be subclasses. They were not: the brief distinguishes them only by length, and a subclass adding no behaviour its parent lacks adds complexity without meaning. A single `Ship` class holds a `ShipType` enum recording the length instead. The Week 3 lecture reaches the same conclusion about this game, noting a ship hierarchy is not really necessary for an example this simple though it would help in a more complex one.'));
body.push(p('The second concerns where each rule lives. Only `Board` decides whether a placement is legal. The fleet generator chooses positions at random but never judges them; it offers each candidate to the board and accepts the verdict. The rule about ships not touching therefore appears in exactly one method and cannot be stated inconsistently in two places, which is the DRY principle from the Week 7 lecture.'));
body.push(p('The third concerns what each class refuses to reveal. `Board` never hands out its ships and the controller offers no route to the board, so the interface receives the board size, the outcome of every cell already fired at, the counts and whether the game is over: enough to draw the grid and announce victory, not enough to expose where the ships are.'));
body.push(p('Finally, the cross-platform look and feel is requested at start-up, because some native ones ignore a button’s background colour and the brief requires blue and red cells. That makes it a requirement rather than a matter of taste.'));

body.push(h1('4. Implementation'));
body.push(h2('4.1 How the application is built'));
body.push(p('Nine classes sit in the four layers described in section 3.'));
body.push(p('In the model, `Coordinate` is an immutable cell location overriding `equals` and `hashCode`, without which a repeated shot would not be recognised. `Ship` holds its type, its cells and the cells hit so far, and its constructor rejects anything that is not a straight unbroken line of the right length, so a ship cannot exist in an invalid state. `Board` owns the ships and the shot history and is the sole authority on placement, rejecting any cell out of bounds or within one row and one column of an existing ship, which is how the diagonal rule is enforced.'));
body.push(p('`RandomShipPlacer` builds a fleet, longest ship first, since placing the four-cell Battleship on an empty board wastes far fewer attempts. Retrying is bounded per ship and per board, and exhaustion raises an exception rather than looping. `GameController` starts and restarts games, passes shots to the board and counts shots and hits separately, and refuses further shots once the fleet is destroyed.'));
body.push(p('In the interface, `CellStyle` returns the colour and mark for one cell and touches no component, which lets the required colours be checked with no display. `GameWindow` gives each cell a listener reporting its own position and redraws the grid from the controller after every shot.'));
body.push(p('Two faults appeared only when the finished application was run, both concerning the mark drawn on a cell rather than its colour; they are described in section 6.2.'));

body.push(h2('4.2 Object-oriented concepts'));
body.push(p('Twelve of the concepts the brief lists are demonstrated, each in a specific place in the code:'));
body.push(table([1800, 7000], [
  ['Concept', 'Where it appears in this project'],
  ['Encapsulation', 'Every field private, collections copied in and returned unmodifiable, ships never exposed'],
  ['Abstraction', '`ShipType` and `ShotResult` name what the program needs without revealing storage'],
  ['Inheritance', '`GameWindow` extends `JFrame`'],
  ['Polymorphism', 'Swing handles listeners and components through their declared types'],
  ['Interfaces', 'Each cell has an `ActionListener`'],
  ['State', 'A ship’s hits, and the board’s shot history'],
  ['Constructors', 'Used to guarantee validity: an invalid `Ship` cannot be created'],
  ['Overloading', 'Two `Board` constructors, and two `createBoard` methods'],
  ['Overriding', '`equals`, `hashCode` and `toString` on `Coordinate`'],
  ['Architectural pattern', 'Model-View-Controller across the four packages'],
  ['Collections', 'A list for positions because order matters, a set for hits because a cell is struck once, a map for history because the duplicate rule is key presence'],
  ['Exceptions', 'Invalid arguments and an exhausted generator are refused rather than ignored'],
]));
body.push(new Paragraph({ text: '', spacing: { after: 160 } }));
body.push(p('Abstract classes, a designed pattern and mutator methods are absent; section 7.4 explains each.'));

body.push(h2('4.3 Final class model'));
body.push(p('The finished model has ten classes where the plan had six. Three of the additions are the enums `ShipType` and `ShotResult` and the appearance class `CellStyle`, each of which exists for a reason given in section 3; the fourth is `App`, the entry point. The direction of every dependency still runs inwards, from the window to the controller to the board, and nothing in the model knows about Swing.'));
body.push(...figure('docs/diagrams/figure-D-final-class-model.png', 560, 401, 'Class model of the final code. Compare with Figure 3: four classes were added, and every dependency still points inwards.'));

body.push(h2('4.4 Differences from the initial plan'));
body.push(p('Four changes were made. The plan left open whether ship types should be subclasses; one class composed with an enum was used instead, for the reason in section 3. The plan expected JUnit; plain Java harnesses were used, leaving no dependencies, at the cost given in section 6.2. The plan did not say where the game-over guard would live; it went to the controller once the difference between a session fact and a grid rule became clear. Finally, the plan treated the interface as one class, and it became two, so the required colours could be tested without opening a window.'));

body.push(h1('5. Annotated Screenshots'));
body.push(p('The figures below were captured from the running application by driving it through its own buttons. The capture harness supplies the fleet generator with a seeded random source, so the same images can be produced again; the application itself uses an unseeded one.'));
body.push(...figure('docs/screenshots/01-new-game.png', 300, 376, 'A new game. All hundred cells unexplored; the status line reads no shots, no hits, six ships. Nothing distinguishes a cell holding a ship from water.'));
body.push(...figure('docs/screenshots/02-in-progress.png', 300, 376, 'A game in progress. Misses are blue and marked o, hits are red and marked X. Cells already fired at are disabled, so a second click does nothing.'));
body.push(...figure('docs/screenshots/03-ship-sunk.png', 300, 376, 'A ship sunk. The Destroyer’s two cells turn a darker red, marked #, and the status line falls to four ships and shows Sunk!. The brief requires hits to be red; the darker shade is an addition separating a completed sinking from a ship still afloat.'));
body.push(...figure('docs/screenshots/04-victory-board.png', 300, 376, 'Victory. The fleet is destroyed in eighty-nine shots and its sixteen cells are now visible: one line of four, two of three and three of two, none touching at an edge or a corner.'));
body.push(...figure('docs/screenshots/05-victory-message.png', 300, 118, 'The victory message. A dialog reports the end of the game and the number of shots.'));
body.push(...figure('docs/screenshots/06-after-restart.png', 300, 376, 'After Restart. New Game clears the board and both counters. The image is identical to Figure 5, which is the point: nothing of the finished game remains, and the new fleet is hidden again.'));

body.push(...figure('docs/screenshots/07-second-victory-board.png', 300, 376, 'The second game, won. Because a cleared board looks exactly like one that has never been played, the second game was also played to the end. Its fleet occupies different cells from Figure 8 and took ninety-two shots, which is what shows that the ships really were placed again rather than reused.'));

body.push(h1('6. Testing'));
body.push(h2('6.1 Unit testing'));
body.push(p('Testing ran alongside development: each class was checked before the next was written, and each check was watched failing before the code that satisfies it was written, because a check that has never failed may be testing the wrong thing. The checks are plain Java classes with a `main` method rather than JUnit tests, which leaves the project free of dependencies at the cost of not being a recognised framework.'));
body.push(p('One hundred and seventy checks pass: ten for `Coordinate`, fifty-two for `Ship`, forty-three for `Board`, twenty-one for `RandomShipPlacer`, thirty-two for `GameController` and twelve for `CellStyle`, covering valid and invalid construction, all four boundaries, overlap, contact in all eight directions, hits, misses, repeated shots, sinking, victory and restart.'));
body.push(p('The generator posed a problem worth describing. `Board` hides where the ships are, correctly for a game, but that left no simple way to check a generated fleet. Rather than weaken the class with an accessor only tests would use, the checks fire at all hundred cells and rebuild the ships as groups of hit cells that touch. That is valid only if ships never touch, so finding six groups of sizes four, three, three, two, two and two proves composition, absence of overlap and the separation rule at once.'));
body.push(p('Writing checks first also found a defect: a board too small for the Battleship produced `IllegalArgumentException: bound must be positive` from inside the Java library, because a non-positive bound reached `Random.nextInt`. The generator now refuses a ship longer than the board first.'));

body.push(h2('6.2 Functional testing'));
body.push(p('Unit checks cannot show the interface behaves correctly, so the finished game was also tested by clicking the grid as a player would. The ten cases run are listed in Appendix B.'));
body.push(p('Two defects were found this way, both invisible to every automated check. The first interface drew a typographic symbol on each fired-at cell and every one appeared as an empty box, because the look and feel’s font had no glyph for them. Switching to plain ASCII revealed a second fault: the marks then showed as three dots, because a Swing button’s wide default margin left too little room for one character. Setting the cell margin to zero fixed it. Unit checks can confirm a colour is blue but not that a character is legible, which is why both kinds of testing were needed.'));

body.push(h1('7. Critical Evaluation'));
body.push(h2('7.1 Does the application meet the requirements?'));
body.push(p('Every requirement from the brief is met, and both chosen extensions work. The strongest evidence for the placement rules is Figure 8: when a game ends the sixteen ship cells become visible and the fleet can be counted on screen, so a requirement otherwise visible only to automated checks can be confirmed by eye. Figure 11 adds the evidence for random placement: a second game, played to its end, hides the fleet in different cells.'));
body.push(p('One requirement was interpreted rather than simply met. The brief does not say whether ships meeting at a corner are touching. This project treats diagonal contact as touching, the stricter reading, so every layout it produces would also be legal under the looser one; but it reduces the number of possible layouts and is an interpretation rather than a fact.'));

body.push(h2('7.2 Was a plan made, and was it followed?'));
body.push(p('A planning document was written before any application code existed and preserved unchanged. The order of work followed it: model, generator, controller, interface, each tested before the next began, and a development log records what was actually done at each stage, including what went wrong. The plan was not followed blindly: four differences are recorded in section 4.4 with their reasons.'));

body.push(h2('7.3 Limitations'));
body.push(p('The checks are plain Java classes rather than JUnit tests: no dependencies, but not a recognised framework, and they stop at the first failure. The window itself has no automated coverage; its correctness rests on ten hand-run functional cases and on holding no rule of its own, and the two rendering faults in section 6.2 show the risk in that.'));
body.push(p('`Board` does not enforce the fleet composition, accepting any ships that fit the placement rules. This lets tests use a board of one or two ships, but a future caller could build a wrong fleet unchallenged. Finally, nothing is saved, and the difficulty levels and two-player mode the brief offers were deliberately deferred.'));

body.push(h2('7.4 Object-oriented concepts'));
body.push(p('Of the fourteen concepts the brief lists, expecting most but not all, this project demonstrates twelve; they are listed in section 4.2.'));
body.push(p('Three are absent by decision rather than oversight. No abstract class is used, because nowhere do two implementations share partial behaviour. No design pattern was deliberately introduced: the observer relationship between a button and its listener comes from Swing. Mutator methods are almost entirely absent, because `Coordinate` and a ship’s positions are immutable; encapsulation was taken to mean controlling change, not providing a setter for every field.'));

body.push(h2('7.5 What could be improved'));
body.push(p('Converting the checks to JUnit would come first, since it is the gap most likely to cost marks while the behaviour checked would not change. After that, naming the type of ship when one is sunk, and adding difficulty levels through the board-size parameter that already exists and is already tested.'));

body.push(h1('8. Conclusion'));
body.push(p('This project produced a working single-player Battleship game that meets every requirement in the brief, together with the two chosen extensions of random placement and a mouse-controlled interface. Separating the rules from the interface allowed the game to be tested thoroughly before anything was drawn, and writing checks before code found two defects that would otherwise have reached the finished application. The single most valuable next step would be to convert those checks to JUnit.'));

body.push(h1('9. References'));
const references = [
  'Anglia Ruskin University (2026) *MOD004883 Advanced Object-Oriented Programming, Element 010: assessment task detail and instructions*. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *MOD004883 Advanced Object-Oriented Programming, Element 010: assessment marking criteria and scheme*. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *Week 3: core object-oriented principles* [lecture slides]. MOD004883 Advanced Object-Oriented Programming. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *Week 4: interfaces and abstract classes* [lecture slides]. MOD004883 Advanced Object-Oriented Programming. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *Week 5: documentation* [lecture slides]. MOD004883 Advanced Object-Oriented Programming. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *Week 6: testing* [lecture slides]. MOD004883 Advanced Object-Oriented Programming. Chelmsford: Anglia Ruskin University, unpublished.',
  'Anglia Ruskin University (2026) *Week 7: DRY, SOLID and collections* [lecture slides]. MOD004883 Advanced Object-Oriented Programming. Chelmsford: Anglia Ruskin University, unpublished.',
  'Oracle (2024) *Java Platform, Standard Edition and Java Development Kit version 23 API specification*. Available at: https://docs.oracle.com/en/java/javase/23/docs/api/ (Accessed: 18 November 2026).',
];
for (const r of references) {
  // Markdown italics are written as *...* in the list above; render them italic.
  const parts = r.split('*');
  body.push(new Paragraph({
    spacing: { after: 160, line: 276 },
    indent: { left: 567, hanging: 567 },
    children: parts.map((t, i) => new TextRun({ text: t, italics: i % 2 === 1 })),
  }));
}

/* ---------- appendices --------------------------------------------------- */

body.push(new Paragraph({ children: [new PageBreak()] }));
body.push(h1('Appendix A: Interview Questions'));
body.push(p('The answers below are the student’s own, written in full sentences as the question sheet requires.'));
const questions = [
  'What are the main principles of Object-Oriented programming?',
  'What is Polymorphism?',
  'When would you create a protected method?',
  'Name three benefits of using encapsulation',
  'What are the key differences and similarities between Interfaces and Abstract Classes?',
  'What is overloading and give an example of overloading.',
  'What is the difference between Functional & Non-Functional testing?',
  'What is the difference between a Queue and a Stack?',
  'Which Design Pattern is typically used for a database connection?',
  'What might be a good example of where to use event driven programming?',
];
const qRows = [['', '']];
questions.forEach((q, i) => {
  qRows.push(['Question ' + (i + 1), q]);
  qRows.push(['Answer ' + (i + 1), '']);
});
qRows.shift();
body.push(new Table({
  columnWidths: [1600, 7200],
  width: { size: 8800, type: WidthType.DXA },
  rows: qRows.map(([a, b]) => new TableRow({
    children: [
      cell(a, { bold: true, width: 1600 }),
      cell(b, { width: 7200 }),
    ],
  })),
}));

body.push(new Paragraph({ children: [new PageBreak()] }));
body.push(h1('Appendix B: Functional Test Cases'));
body.push(p('These ten cases were run by hand on the finished application; they are discussed in section 6.2.'));
body.push(table([700, 2600, 3100, 2400], [
  ['#', 'Action', 'Expected', 'Observed'],
  ['F1', 'Start the application', '100 unexplored cells, no shots, six ships', 'Pass, Figure 5'],
  ['F2', 'Click an empty cell', 'Blue; shots +1, hits unchanged', 'Pass, Figure 6'],
  ['F3', 'Click a ship cell', 'Red; shots +1, hits +1', 'Pass, Figure 6'],
  ['F4', 'Click a cell already fired at', 'Nothing changes', 'Pass; cell disabled'],
  ['F5', 'Hit a ship’s last cell', 'Dark red, ships left falls, Sunk!', 'Pass, Figure 7'],
  ['F6', 'Sink the whole fleet', 'Victory dialog and total shots', 'Pass, Figures 8 and 9'],
  ['F7', 'Click after victory', 'Nothing happens', 'Pass; grid disabled'],
  ['F8', 'Press New Game', 'Board and counters cleared, new fleet', 'Pass, Figure 10'],
  ['F9', 'Count the revealed fleet', 'One 4, two 3s, three 2s, none touching', 'Pass, Figure 8'],
  ['F10', 'Win a second game', 'A different fleet in different cells', 'Pass, Figure 11'],
]));

/* ---------- document ---------------------------------------------------- */

const heading = (size, before, after) => ({
  run: { font: BODY_FONT, size, bold: true, color: '000000' },
  paragraph: { spacing: { before, after }, keepNext: true },
});

const doc = new Document({
  creator: 'MOD004883 Component 2',
  title: 'A Single-Player Battleship Game in Java',
  features: { updateFields: true },
  numbering: {
    config: [{
      reference: 'report-bullets',
      levels: [{
        level: 0, format: LevelFormat.BULLET, text: '•',
        alignment: AlignmentType.LEFT,
        style: { paragraph: { indent: { left: 567, hanging: 283 } } },
      }],
    }],
  },
  styles: {
    default: {
      document: { run: { font: BODY_FONT, size: 22, color: '000000' } },
      title: heading(44, 0, 240),
      heading1: heading(30, 320, 160),
      heading2: heading(25, 240, 120),
    },
    paragraphStyles: [{
      // Named explicitly as well as in the document defaults: some readers ignore
      // the defaults and fall back to their own font otherwise.
      id: 'Normal', name: 'Normal', quickFormat: true,
      run: { font: BODY_FONT, size: 22, color: '000000' },
      paragraph: { spacing: { after: 160, line: 276 } },
    }, {
      id: 'Caption', name: 'Caption', basedOn: 'Normal', next: 'Normal',
      quickFormat: true,
      run: { font: BODY_FONT, size: 19, italics: true, color: '333333' },
      paragraph: { alignment: AlignmentType.CENTER, spacing: { after: 240 } },
    }],
  },
  sections: [{
    properties: { type: SectionType.CONTINUOUS },
    footers: {
      default: new Footer({
        children: [new Paragraph({
          alignment: AlignmentType.CENTER,
          children: [new TextRun({ children: [PageNumber.CURRENT], font: BODY_FONT, size: 18 })],
        })],
      }),
    },
    children: [...cover, ...frontMatter, ...body],
  }],
});

Packer.toBuffer(doc).then((buffer) => {
  const out = path.join(ROOT, 'docs', 'report', 'MOD004883-Component2-Report.docx');
  fs.writeFileSync(out, buffer);
  console.log('written: ' + out);
});
