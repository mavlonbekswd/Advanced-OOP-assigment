# 5. Annotated Screenshots

*Target: 100 words of captions and commentary. Status: complete.*

---

The images below were captured from the running application. They are stored in
`docs/screenshots/`. Each must be given a numbered caption in the Word document and
be referred to by number in the surrounding text.

**Figure 1 — A new game (`01-new-game.png`).**
All one hundred cells are unexplored, and the status line reads
`Shots: 0   Hits: 0   Ships left: 6`. The fleet is hidden: nothing on screen
distinguishes a cell holding a ship from empty water.

**Figure 2 — A game in progress (`02-in-progress.png`).**
Misses are shown in blue and marked `o`; hits are shown in red and marked `X`. The
status line has updated to seven shots and two hits. Cells already fired at are
disabled, so a second click on them does nothing.

**Figure 3 — A ship sunk (`03-ship-sunk.png`).**
The two cells of a Destroyer are shown in a darker red and marked `#`, and the
status line reports `Ships left: 4` followed by the message `Sunk!`. The brief
requires hits to be red; the darker shade is an addition, so that a completed
sinking can be told apart from a ship still afloat.

**Figure 4 — Victory (`04-victory-board.png`).**
The whole fleet has been destroyed in eighty-nine shots. The sixteen ship cells are
now visible as red, and the shapes confirm the required fleet: one line of four
cells, two lines of three and three lines of two, with no two ships touching at an
edge or a corner. The status line reads `All ships sunk in 89 shots` and every
remaining cell is disabled.

**Figure 5 — The victory message (`05-victory-message.png`).**
A dialog announces the end of the game and the number of shots taken.

**Figure 6 — After Restart (`06-after-restart.png`).**
Pressing New Game clears the board and the counters completely. A fresh fleet has
been generated in new positions, which cannot be seen because it is hidden again.

## A note on how these were captured

These images were produced by driving the real application: the window was opened,
cells were clicked in sequence, and the live window was rendered to a PNG file at
each state of interest. The tool that does this is kept in `docs/evidence/` so the
images can be reproduced. It uses the application exactly as a player would, through
the same buttons, and changes nothing about how the game behaves.

Taking the same images by hand with the operating system's own screen capture would
be equally valid and is worth doing for the final submission, since the brief asks
for screen grabs.
