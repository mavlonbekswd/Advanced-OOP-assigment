package battleship.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Owns placed ships and shot history on a square board.
 * Ships may not touch, including diagonally. Placement closes after the first shot.
 * Fleet composition is left to the game setup service.
 */
public final class Board {
    public static final int DEFAULT_SIZE = 10;
    private final int size;
    private final List<Ship> ships = new ArrayList<>();
    private final Map<Coordinate, ShotResult> shots = new LinkedHashMap<>();

    /** Creates the standard 10 by 10 board. */
    public Board() { this(DEFAULT_SIZE); }

    /** @param size positive number of rows and columns */
    public Board(int size) {
        if (size <= 0) throw new IllegalArgumentException("Board size must be positive");
        this.size = size;
    }

    /** @return number of rows and columns */
    public int getSize() { return size; }

    /**
     * Checks bounds, separation and whether setup is still open.
     * @param candidate ship to inspect
     * @return whether placement would succeed
     */
    public boolean canPlaceShip(Ship candidate) {
        Objects.requireNonNull(candidate, "Ship is required");
        if (!shots.isEmpty()) return false;
        for (Coordinate cell : candidate.getPositions()) {
            if (!cell.isWithin(size)) return false;
            for (Ship existing : ships) {
                for (Coordinate occupied : existing.getPositions()) {
                    if (Math.abs((long) cell.getRow() - occupied.getRow()) <= 1
                            && Math.abs((long) cell.getColumn() - occupied.getColumn()) <= 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Adds a fresh private copy, so external Ship mutations cannot affect play.
     * @param candidate ship whose type and locations are copied
     * @throws IllegalArgumentException for invalid placement
     */
    public void placeShip(Ship candidate) {
        if (!canPlaceShip(candidate)) throw new IllegalArgumentException("Invalid ship placement");
        ships.add(new Ship(candidate.getType(), candidate.getPositions()));
    }

    /**
     * Fires at a cell. Requires at least one placed ship; invalid input changes nothing.
     * @param cell zero-based location inside the board
     * @return new shot outcome, or ALREADY_SHOT for a duplicate
     * @throws IllegalArgumentException for out-of-bounds cells
     * @throws IllegalStateException when the board has no ships
     */
    public ShotResult fireAt(Coordinate cell) {
        Objects.requireNonNull(cell, "Cell is required");
        if (!cell.isWithin(size)) throw new IllegalArgumentException("Shot is outside the board");
        if (ships.isEmpty()) throw new IllegalStateException("Place ships before firing");
        if (shots.containsKey(cell)) return ShotResult.ALREADY_SHOT;
        ShotResult result = ShotResult.MISS;
        for (Ship ship : ships) {
            if (ship.registerHit(cell)) {
                result = ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
                break;
            }
        }
        shots.put(cell, result);
        return result;
    }

    /** @return immutable snapshot of shots; no hidden ship locations are exposed */
    public Map<Coordinate, ShotResult> getShots() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(shots));
    }

    /** @return number of placed ships */
    public int getShipCount() { return ships.size(); }

    /** @return number of sunk ships */
    public int getSunkShipCount() {
        return (int) ships.stream().filter(Ship::isSunk).count();
    }

    /** @return true only for a nonempty fleet with every ship sunk */
    public boolean allShipsSunk() {
        return !ships.isEmpty() && getSunkShipCount() == ships.size();
    }
}
