package battleship.model;

import java.util.Objects;

/**
 * What the player is allowed to know about one ship: its kind and whether it has
 * been sunk.
 *
 * <p>This is the whole reason the class exists. The interface wants to list the
 * fleet and cross off the ships as they go down, but handing it a {@link Ship}
 * would hand it the squares that ship occupies, and the game would be given away.
 * A status carries the two facts the player has already earned and nothing else.
 *
 * <p>OOP: Encapsulation and information hiding. The board answers a question about
 * its ships without exposing the ships themselves.
 *
 * <p>OOP: State and immutability. A status is a reading taken at one moment. It
 * never changes afterwards, so a window that keeps one cannot show a sunk ship
 * floating again.
 */
public final class ShipStatus {

    private final ShipType type;
    private final boolean sunk;

    /**
     * Records the state of one ship.
     *
     * @param type the kind of ship this describes
     * @param sunk whether every one of its squares has been hit
     * @throws NullPointerException if type is null
     */
    public ShipStatus(ShipType type, boolean sunk) {
        this.type = Objects.requireNonNull(type, "Ship type is required");
        this.sunk = sunk;
    }

    /**
     * Returns the kind of ship this describes.
     *
     * @return the ship's type
     */
    public ShipType getType() {
        return type;
    }

    /**
     * Returns how many squares the ship occupies.
     *
     * <p>The length follows from the type. It is offered here so the interface can
     * draw one block per square without having to reach into the enum itself.
     *
     * @return the number of squares this ship covers
     */
    public int getLength() {
        return type.getLength();
    }

    /**
     * Reports whether the ship has been destroyed.
     *
     * @return true once every square of the ship has been hit
     */
    public boolean isSunk() {
        return sunk;
    }

    /**
     * Describes this status for a log or an error message.
     *
     * @return the type followed by its condition
     */
    @Override
    public String toString() {
        return type + (sunk ? " (sunk)" : " (afloat)");
    }
}
