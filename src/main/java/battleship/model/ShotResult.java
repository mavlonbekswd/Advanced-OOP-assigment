package battleship.model;

/**
 * Outcome of a shot; duplicate shots never change board state.
 *
 * <p>OOP: Abstraction and state. Naming the four possible outcomes lets the
 * controller and interface react to a shot without knowing how the board stores
 * ships, and makes an unhandled outcome a visible omission rather than a silent one.
 */
public enum ShotResult {
    /** The cell held no part of any ship. */
    MISS,
    /** The cell held part of a ship that is still afloat. */
    HIT,
    /** The cell held the last unstruck part of a ship. */
    SUNK,
    /** The cell had already been fired at; nothing changed. */
    ALREADY_SHOT
}
