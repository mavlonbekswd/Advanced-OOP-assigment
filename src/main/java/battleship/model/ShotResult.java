package battleship.model;

/**
 * Outcome of a shot; duplicate shots never change board state.
 *
 * <p>OOP: Abstraction and state. Naming the four possible outcomes lets the
 * controller and interface react to a shot without knowing how the board stores
 * ships, and makes an unhandled outcome a visible omission rather than a silent one.
 */
public enum ShotResult {
    MISS, HIT, SUNK, ALREADY_SHOT
}
