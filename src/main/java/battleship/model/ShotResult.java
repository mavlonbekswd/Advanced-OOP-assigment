package battleship.model;

/** Outcome of a shot; duplicate shots never change board state. */
public enum ShotResult {
    MISS, HIT, SUNK, ALREADY_SHOT
}
