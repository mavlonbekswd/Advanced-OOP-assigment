package battleship.model;

/** Fleet types and their required lengths from the assignment brief. */
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
