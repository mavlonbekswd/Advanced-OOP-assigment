package battleship.model;

/**
 * Fleet types and their required lengths from the assignment brief.
 *
 * <p>OOP: Abstraction. The three kinds of ship differ only by the number of cells
 * they occupy, so that single distinguishing property is modelled here as a fixed
 * set of named constants rather than as a class hierarchy.
 */
public enum ShipType {
    BATTLESHIP(4), CRUISER(3), DESTROYER(2);

    private final int length;

    ShipType(int length) {
        this.length = length;
    }

    /** @return number of cells occupied by this type */
    public int getLength() {
        return length;
    }
}
