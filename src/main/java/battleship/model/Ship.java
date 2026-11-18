package battleship.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A placed, straight ship with immutable positions and encapsulated hit state.
 * Board bounds and relationships to other ships are the Board's responsibility.
 *
 * <p>OOP: Encapsulation. The occupied cells and the set of recorded hits are
 * private; callers receive an unmodifiable view and cannot corrupt a ship's state.
 *
 * <p>OOP: Composition over inheritance. A ship holds a {@link ShipType} instead of
 * extending a base class, because the three types currently differ only by length
 * and add no distinct behaviour that a subclass would override.
 *
 * <p>OOP: Constructors and state. The constructor is the only place a ship can be
 * given its shape, and it rejects every invalid shape, so no object of this class
 * can exist in an illegal state.
 *
 * <p>OOP: Choice of collection. Positions are a List because order and contiguity
 * matter; hits are a Set because a cell can only be struck once, which gives
 * duplicate-hit handling for free.
 */
public final class Ship {
    private final ShipType type;
    private final List<Coordinate> positions;
    private final Set<Coordinate> hits = new HashSet<>();

    /**
     * Creates a ship from distinct, contiguous horizontal or vertical cells.
     * Input order does not matter. The supplied collection is defensively copied.
     *
     * @param type required ship type
     * @param positions occupied cells; count must match the type's length
     * @throws NullPointerException if type, positions or any cell is null
     * @throws IllegalArgumentException if the cells do not form a valid ship
     */
    public Ship(ShipType type, List<Coordinate> positions) {
        this.type = Objects.requireNonNull(type, "Ship type is required");
        Objects.requireNonNull(positions, "Positions are required");
        List<Coordinate> copy = new ArrayList<>(positions);
        for (Coordinate cell : copy) {
            Objects.requireNonNull(cell, "Ship cells cannot be null");
        }
        if (copy.size() != type.getLength()
                || new HashSet<>(copy).size() != copy.size()) {
            throw new IllegalArgumentException("Distinct cell count must match ship length");
        }
        boolean horizontal = copy.stream().allMatch(c -> c.getRow() == copy.get(0).getRow());
        boolean vertical = copy.stream().allMatch(c -> c.getColumn() == copy.get(0).getColumn());
        if (!horizontal && !vertical) {
            throw new IllegalArgumentException("Ship must be horizontal or vertical");
        }
        copy.sort(horizontal
                ? java.util.Comparator.comparingInt(Coordinate::getColumn)
                : java.util.Comparator.comparingInt(Coordinate::getRow));
        for (int i = 1; i < copy.size(); i++) {
            long previous = horizontal ? copy.get(i - 1).getColumn() : copy.get(i - 1).getRow();
            long current = horizontal ? copy.get(i).getColumn() : copy.get(i).getRow();
            if (current - previous != 1) {
                throw new IllegalArgumentException("Ship cells must be contiguous");
            }
        }
        this.positions = Collections.unmodifiableList(copy);
    }

    /**
     * Returns which kind of ship this is.
     *
     * @return ship type
     */
    public ShipType getType() { return type; }

    /**
     * Returns the cells this ship occupies.
     *
     * @return immutable occupied cells in coordinate order
     */
    public List<Coordinate> getPositions() { return positions; }

    /**
     * Returns how much of this ship has been struck.
     *
     * @return count of distinct successful hits
     */
    public int getHitCount() { return hits.size(); }

    /**
     * Reports whether this ship covers the given cell.
     *
     * @param cell queried cell
     * @return true if this ship occupies that cell
     */
    public boolean occupies(Coordinate cell) {
        return positions.contains(Objects.requireNonNull(cell, "Cell is required"));
    }

    /**
     * Records a hit only once. Misses and repeated hits leave state unchanged.
     * @param cell shot location
     * @return true only when a new hit is recorded
     */
    public boolean registerHit(Coordinate cell) {
        return occupies(cell) && hits.add(cell);
    }

    /**
     * Reports whether this ship has been destroyed.
     *
     * @return true when every occupied cell has been hit
     */
    public boolean isSunk() { return hits.size() == positions.size(); }
}
