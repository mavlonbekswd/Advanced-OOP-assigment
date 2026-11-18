package battleship.service;

import battleship.model.Board;
import battleship.model.Coordinate;
import battleship.model.Ship;
import battleship.model.ShipType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Builds a complete, legally separated fleet at random positions.
 *
 * <p>OOP: Abstraction and single responsibility. This service knows the fleet the
 * assignment requires and how to choose positions, while {@link Board} remains the
 * only authority on whether a position is legal. The rules are therefore stated
 * once (DRY) instead of being duplicated here.
 *
 * <p>OOP: Dependency injection. The source of randomness is supplied through the
 * constructor, so a test can pass a seeded {@link Random} and obtain a repeatable
 * fleet without the production code exposing any test-only entry point.
 */
public final class RandomShipPlacer {

    /**
     * The exact fleet from the assignment brief, longest first.
     * Placing large ships while the board is empty reduces wasted attempts.
     */
    private static final List<ShipType> FLEET = Arrays.asList(
            ShipType.BATTLESHIP,
            ShipType.CRUISER, ShipType.CRUISER,
            ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER);

    /** Random positions tried for one ship before the whole board is abandoned. */
    private static final int ATTEMPTS_PER_SHIP = 200;

    /** Complete boards attempted before generation is reported as failed. */
    private static final int BOARD_ATTEMPTS = 100;

    private final Random random;

    /** Creates a placer driven by an unseeded source of randomness. */
    public RandomShipPlacer() {
        this(new Random());
    }

    /**
     * Creates a placer driven by the supplied source of randomness.
     *
     * @param random source of randomness; a seeded instance makes fleets repeatable
     * @throws NullPointerException if random is null
     */
    public RandomShipPlacer(Random random) {
        this.random = Objects.requireNonNull(random, "Random source is required");
    }

    /**
     * Creates a standard 10 by 10 board holding the complete required fleet.
     *
     * @return a board with one Battleship, two Cruisers and three Destroyers,
     *         none overlapping and none touching
     * @throws IllegalStateException if no valid fleet could be generated
     */
    public Board createBoard() {
        return createBoard(Board.DEFAULT_SIZE);
    }

    /**
     * Creates a board of the given size holding the complete required fleet.
     *
     * <p>OOP: Overloading. The no-argument form states the assignment's standard
     * board; this form exists so the generator can be exercised on sizes where the
     * fleet does not fit, proving that failure is reported rather than looped on.
     *
     * @param size number of rows and columns
     * @return a filled board with no overlapping or touching ships
     * @throws IllegalArgumentException if size is not positive
     * @throws IllegalStateException if no valid fleet could be generated
     */
    public Board createBoard(int size) {
        for (int attempt = 0; attempt < BOARD_ATTEMPTS; attempt++) {
            Board board = new Board(size);
            if (fillFleet(board)) {
                return board;
            }
        }
        throw new IllegalStateException("Could not generate a valid fleet on a " + size
                + " by " + size + " board");
    }

    /**
     * Attempts to place every required ship on an empty board.
     *
     * @param board board to fill
     * @return true only if the whole fleet was placed
     */
    private boolean fillFleet(Board board) {
        for (ShipType type : FLEET) {
            if (!placeOne(board, type)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tries random positions for one ship until the board accepts one.
     *
     * @param board board being filled
     * @param type ship type to place
     * @return true only if the ship was placed
     */
    private boolean placeOne(Board board, ShipType type) {
        if (type.getLength() > board.getSize()) {
            return false;
        }
        for (int attempt = 0; attempt < ATTEMPTS_PER_SHIP; attempt++) {
            Ship candidate = new Ship(type, randomCells(board.getSize(), type.getLength()));
            if (board.canPlaceShip(candidate)) {
                board.placeShip(candidate);
                return true;
            }
        }
        return false;
    }

    /**
     * Chooses a random horizontal or vertical run of cells inside the board.
     *
     * @param boardSize number of rows and columns
     * @param length number of cells the ship occupies
     * @return cells that are always in bounds, straight and contiguous
     */
    private List<Coordinate> randomCells(int boardSize, int length) {
        boolean horizontal = random.nextBoolean();
        int freeSpan = boardSize - length + 1;
        int row = random.nextInt(horizontal ? boardSize : freeSpan);
        int column = random.nextInt(horizontal ? freeSpan : boardSize);
        List<Coordinate> cells = new ArrayList<>();
        for (int offset = 0; offset < length; offset++) {
            cells.add(horizontal
                    ? new Coordinate(row, column + offset)
                    : new Coordinate(row + offset, column));
        }
        return cells;
    }
}
