package battleship.view;

import battleship.model.ShotResult;
import battleship.view.CellStyle.Marks;

import java.awt.Color;
import java.awt.Font;

/**
 * Dependency-free checks for how a cell is drawn; this harness is not JUnit.
 *
 * <p>No window is opened here. The appearance of a cell is pure data, so it can be
 * checked without a display, which is why it was separated from the window class.
 * The mark a cell carries depends on what the font can draw, and the font is passed
 * in rather than looked up, so that path is checked here too.
 */
public final class CellStyleSelfTest {
    private static int checks;

    public static void main(String[] args) {
        // A cell that has not been fired at shows neither of the two result colours.
        Color water = CellStyle.colourFor(null);
        check(!water.equals(CellStyle.colourFor(ShotResult.MISS)), "Water is not a miss");
        check(!water.equals(CellStyle.colourFor(ShotResult.HIT)), "Water is not a hit");

        // The brief requires a miss to be blue and a hit to be red.
        Color miss = CellStyle.colourFor(ShotResult.MISS);
        check(miss.getBlue() > miss.getRed() && miss.getBlue() > miss.getGreen(), "Miss is blue");

        Color hit = CellStyle.colourFor(ShotResult.HIT);
        check(hit.getRed() > hit.getBlue() && hit.getRed() > hit.getGreen(), "Hit is red");

        // A sunk ship stays red, as the brief requires, but darker, so that a
        // completed sinking can be told apart from a ship still afloat.
        Color sunk = CellStyle.colourFor(ShotResult.SUNK);
        check(sunk.getRed() > sunk.getBlue() && sunk.getRed() > sunk.getGreen(), "Sunk is red");
        check(sunk.getRed() < hit.getRed(), "Sunk is a darker red than a hit");

        // An unexplored cell carries no mark whichever set is in use.
        check(CellStyle.labelFor(null, Marks.PLAIN).isEmpty(), "Unshot cell has no plain mark");
        check(CellStyle.labelFor(null, Marks.SYMBOLS).isEmpty(), "Unshot cell has no symbol");

        // The plain set is the one the game fell back on when a font could not draw
        // the symbols, so these three characters are fixed deliberately.
        check("o".equals(CellStyle.labelFor(ShotResult.MISS, Marks.PLAIN)), "Plain miss is o");
        check("X".equals(CellStyle.labelFor(ShotResult.HIT, Marks.PLAIN)), "Plain hit is X");
        check("#".equals(CellStyle.labelFor(ShotResult.SUNK, Marks.PLAIN)), "Plain sunk is #");

        // Whichever set is chosen, every mark is exactly one character, so a cell
        // never has to shrink its text to fit.
        for (ShotResult state : new ShotResult[] { ShotResult.MISS, ShotResult.HIT, ShotResult.SUNK }) {
            check(CellStyle.labelFor(state, Marks.PLAIN).length() == 1, "One plain character: " + state);
            check(CellStyle.labelFor(state, Marks.SYMBOLS).length() == 1, "One symbol: " + state);
            check(!CellStyle.labelFor(state, Marks.SYMBOLS)
                    .equals(CellStyle.labelFor(state, Marks.PLAIN)), "The sets differ: " + state);
        }

        // Within a set the three marks must differ, or the grid could not be read
        // by anyone who cannot tell the two reds apart.
        checkDistinct(Marks.PLAIN);
        checkDistinct(Marks.SYMBOLS);

        // A font that cannot be consulted at all is the case that caused the original
        // defect: the answer has to be the plain set, never a symbol drawn blind.
        check(CellStyle.marksFor(null) == Marks.PLAIN, "No font means plain marks");

        // A font that can draw every symbol gets them; one that cannot falls back.
        Font dialog = new Font(Font.DIALOG, Font.PLAIN, 12);
        Marks chosen = CellStyle.marksFor(dialog);
        check(chosen == Marks.SYMBOLS || chosen == Marks.PLAIN, "A font gets one of the two sets");
        check(chosen != Marks.SYMBOLS || canDrawEverySymbol(dialog),
                "Symbols are only chosen when the font can draw them all");
        check(chosen != Marks.PLAIN || !canDrawEverySymbol(dialog),
                "The fallback is only taken when a symbol is missing");

        // The board never records a duplicate as the state of a cell, so asking for
        // its appearance is a programming error rather than something to draw.
        rejects(IllegalArgumentException.class, () -> CellStyle.colourFor(ShotResult.ALREADY_SHOT));
        rejects(IllegalArgumentException.class,
                () -> CellStyle.labelFor(ShotResult.ALREADY_SHOT, Marks.PLAIN));
        rejects(NullPointerException.class, () -> CellStyle.labelFor(ShotResult.MISS, null));

        System.out.println("PASS: " + checks + " CellStyle self-checks");
    }

    /**
     * Confirms that no two states share a mark within one set.
     *
     * @param marks the set to inspect
     */
    private static void checkDistinct(Marks marks) {
        String miss = CellStyle.labelFor(ShotResult.MISS, marks);
        String hit = CellStyle.labelFor(ShotResult.HIT, marks);
        String sunk = CellStyle.labelFor(ShotResult.SUNK, marks);
        check(!miss.equals(hit) && !hit.equals(sunk) && !miss.equals(sunk),
                "Three different marks in " + marks);
    }

    /**
     * Asks the font directly, so the check does not simply repeat the class it tests.
     *
     * @param font the font to ask
     * @return whether it can draw all three symbols
     */
    private static boolean canDrawEverySymbol(Font font) {
        for (ShotResult state : new ShotResult[] { ShotResult.MISS, ShotResult.HIT, ShotResult.SUNK }) {
            if (!font.canDisplay(CellStyle.labelFor(state, Marks.SYMBOLS).codePointAt(0))) {
                return false;
            }
        }
        return true;
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
        checks++;
    }

    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Unexpected exception", error);
            checks++;
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
