package battleship.view;

import battleship.model.ShotResult;

import java.awt.Color;

/**
 * Dependency-free checks for how a cell is drawn; this harness is not JUnit.
 *
 * <p>No window is opened here. The appearance of a cell is pure data, so it can be
 * checked without a display, which is why it was separated from the window class.
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

        check(CellStyle.labelFor(null).isEmpty(), "Unshot cell has no label");
        check(!CellStyle.labelFor(ShotResult.MISS).isEmpty(), "Miss is labelled");
        check(!CellStyle.labelFor(ShotResult.HIT).isEmpty(), "Hit is labelled");
        check(!CellStyle.labelFor(ShotResult.SUNK).isEmpty(), "Sunk is labelled");

        // The board never records a duplicate as the state of a cell, so asking for
        // its appearance is a programming error rather than something to draw.
        rejects(IllegalArgumentException.class, () -> CellStyle.colourFor(ShotResult.ALREADY_SHOT));
        rejects(IllegalArgumentException.class, () -> CellStyle.labelFor(ShotResult.ALREADY_SHOT));

        System.out.println("PASS: " + checks + " CellStyle self-checks");
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
