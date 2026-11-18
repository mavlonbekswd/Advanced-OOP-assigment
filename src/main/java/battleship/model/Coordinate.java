package battleship.model;

import java.util.Objects;

/**
 * Represents a cell location on a Battleship board.
 * Rows and columns are zero-based (the first cell is 0, 0).
 *
 * <p>OOP: Encapsulation protects the immutable position state.
 * The constructor initialises the object; getters expose its values.
 */
public final class Coordinate {
    private final int row;
    private final int column;

    /**
     * Creates a coordinate. Bounds are checked by the board, not here.
     *
     * @param row zero-based row index
     * @param column zero-based column index
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row this coordinate sits on.
     *
     * @return zero-based row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column this coordinate sits on.
     *
     * @return zero-based column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Checks whether this coordinate is inside a square board.
     *
     * @param boardSize number of rows and columns; must be positive
     * @return true if row and column are within the board boundaries
     */
    public boolean isWithin(int boardSize) {
        if (boardSize <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        return row >= 0 && row < boardSize
                && column >= 0 && column < boardSize;
    }

    /**
     * Compares two coordinates by their row and column values.
     *
     * @param other the object to compare
     * @return true if both row and column are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Coordinate)) {
            return false;
        }
        Coordinate that = (Coordinate) other;
        return row == that.row && column == that.column;
    }

    /**
     * @return hash code consistent with equals; useful for HashSet/HashMap
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * @return a readable representation of this coordinate
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
