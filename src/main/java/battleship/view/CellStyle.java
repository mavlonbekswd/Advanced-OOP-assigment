package battleship.view;

import battleship.model.ShotResult;

import java.awt.Color;

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
     * Returns the short text shown on a cell, so that the grid can still be read by
     * someone who cannot distinguish the two colours.
     *
     * <p>Plain ASCII characters are used deliberately. An earlier version used
     * typographic symbols, which the font of the chosen look and feel could not
     * render; on screen every marked cell showed an empty box instead.
     *
     * @param result what the player knows about the cell, or null if never fired at
     * @return the label for that state, empty for an unexplored cell
     * @throws IllegalArgumentException if given {@link ShotResult#ALREADY_SHOT}
     */
    public static String labelFor(ShotResult result) {
        if (result == null) {
            return "";
        }
        switch (result) {
            case MISS: return "o";
            case HIT: return "X";
            case SUNK: return "#";
            default: throw new IllegalArgumentException("Not a cell state: " + result);
        }
    }
}
