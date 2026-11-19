package battleship.view;

import battleship.model.ShotResult;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

/**
 * Decides how one cell of the grid should look.
 *
 * <p>OOP: Abstraction and separation of concerns. The appearance of a cell depends
 * only on what the player has already learned about it, so it is pure data and lives
 * apart from the window. That separation is what allows the colours required by the
 * brief to be checked automatically, with no display and no window open.
 *
 * <p>This class is never instantiated; it exists to hold that one decision in a
 * single place, so the grid and any future legend cannot disagree about a colour.
 *
 * <p>Nothing here looks the font up for itself. A caller that knows which font will
 * actually draw the text asks {@link #marksFor(Font)} once and passes the answer in,
 * which keeps the class free of Swing and leaves every path testable.
 */
public final class CellStyle {

    /** Unexplored water, before the player has fired at the cell. */
    private static final Color WATER = new Color(0xB0, 0xBE, 0xC5);

    /** A miss, which the assessment brief requires to be blue. */
    private static final Color MISS = new Color(0x1E, 0x88, 0xE5);

    /** A hit on a ship that is still afloat, which the brief requires to be red. */
    private static final Color HIT = new Color(0xE5, 0x39, 0x35);

    /** A hit that completed a sinking: still red, but darker. */
    private static final Color SUNK = new Color(0x8E, 0x1C, 0x1A);

    /**
     * The colour every mark is drawn in. One colour serves all three states because
     * white is the only one that stays readable on the blue of a miss and on both
     * reds; the checks measure that rather than assume it.
     */
    private static final Color MARK = Color.WHITE;

    // The symbols are written as escapes so that this file stays plain ASCII and
    // compiles the same way whatever character set the compiler defaults to.

    /** A small dot for a miss: nothing was found here. */
    private static final String MISS_SYMBOL = "\u2022";

    /** A cross for a hit. */
    private static final String HIT_SYMBOL = "\u2715";

    /** A filled square for a ship that has been destroyed outright. */
    private static final String SUNK_SYMBOL = "\u25A0";

    /**
     * Which set of marks a cell should carry.
     *
     * <p>The first version of the interface drew symbols without asking whether they
     * could be drawn, and the font of the chosen look and feel had no glyph for them,
     * so every marked cell showed an empty box. The project then retreated to plain
     * ASCII. This enum is the better answer to the same problem: the symbols are used
     * when the font can draw them, and the ASCII marks remain as the fallback, so a
     * cell is never blank and never a box.
     */
    public enum Marks {
        /** The drawn symbols, used when the font has a glyph for each of them. */
        SYMBOLS,
        /** Plain ASCII, which any font can draw. */
        PLAIN
    }

    private CellStyle() {
        // Holder of one decision: no instance is needed.
    }

    /**
     * Returns the colour a cell should be painted.
     *
     * @param result what the player knows about the cell, or null if never fired at
     * @return the colour for that state
     * @throws IllegalArgumentException if given {@link ShotResult#ALREADY_SHOT},
     *         which is the answer to a repeated shot and never the state of a cell
     */
    public static Color colourFor(ShotResult result) {
        if (result == null) {
            return WATER;
        }
        switch (result) {
            case MISS: return MISS;
            case HIT: return HIT;
            case SUNK: return SUNK;
            default: throw new IllegalArgumentException("Not a cell state: " + result);
        }
    }

    /**
     * Returns the colour a cell's mark should be drawn in.
     *
     * <p>The mark carries the same information as the colour of the cell, for a
     * player who cannot tell blue from red or one red from the other, so it is of no
     * use unless it is legible. It is drawn in one colour on every cell rather than
     * one per state, because a second colour would have to be read against the first
     * to mean anything.
     *
     * @return the colour of the mark
     */
    public static Color markColour() {
        return MARK;
    }

    /**
     * Chooses the set of marks a given font can actually draw.
     *
     * <p>It is all three symbols or none of them. A grid showing two symbols and one
     * ASCII character would look like a mistake, and the three marks have to be
     * distinguishable from each other more than they have to be pretty.
     *
     * @param font the font that will draw the marks, or null if it is not known
     * @return {@link Marks#SYMBOLS} only when every symbol has a glyph in that font
     */
    public static Marks marksFor(Font font) {
        if (font == null) {
            return Marks.PLAIN;
        }
        for (String symbol : new String[] { MISS_SYMBOL, HIT_SYMBOL, SUNK_SYMBOL }) {
            if (!font.canDisplay(symbol.codePointAt(0))) {
                return Marks.PLAIN;
            }
        }
        return Marks.SYMBOLS;
    }

    /**
     * Returns the mark shown on a cell, so that the grid can still be read by
     * someone who cannot distinguish the two colours.
     *
     * @param result what the player knows about the cell, or null if never fired at
     * @param marks which set of marks to use, from {@link #marksFor(Font)}
     * @return the mark for that state, empty for an unexplored cell
     * @throws IllegalArgumentException if given {@link ShotResult#ALREADY_SHOT}
     * @throws NullPointerException if marks is null
     */
    public static String labelFor(ShotResult result, Marks marks) {
        Objects.requireNonNull(marks, "Mark set is required");
        if (result == null) {
            return "";
        }
        boolean drawn = marks == Marks.SYMBOLS;
        switch (result) {
            case MISS: return drawn ? MISS_SYMBOL : "o";
            case HIT: return drawn ? HIT_SYMBOL : "X";
            case SUNK: return drawn ? SUNK_SYMBOL : "#";
            default: throw new IllegalArgumentException("Not a cell state: " + result);
        }
    }
}
