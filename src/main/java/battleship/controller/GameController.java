package battleship.controller;

import battleship.model.Board;
import battleship.model.Coordinate;
import battleship.model.ShotResult;
import battleship.service.RandomShipPlacer;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Runs one game: starting, restarting, taking shots and deciding when it is won.
 *
 * <p>OOP: Architectural pattern. This is the controller of the Model-View-Controller
 * split described in the architecture notes. It holds no rules about what a legal
 * placement or a hit is, which belong to {@link Board}, and it draws nothing, which
 * belongs to the interface. Its own subject is the lifecycle of a game.
 *
 * <p>OOP: Abstraction. The interface is told only what it needs in order to draw:
 * the board size, the outcome of each cell already fired at, the running counts and
 * whether the game is over. The board itself, and therefore the hidden positions of
 * the ships, is never handed out.
 *
 * <p>OOP: Dependency injection. The fleet generator is supplied through a
 * constructor, so a test can start a game from a known seed.
 */
public final class GameController {

    private final RandomShipPlacer placer;
    private Board board;
    private int shotCount;
    private int hitCount;

    /** Creates a controller that generates fleets from an unseeded source. */
    public GameController() {
        this(new RandomShipPlacer());
    }

    /**
     * Creates a controller using the supplied fleet generator.
     *
     * @param placer generator used for every new game
     * @throws NullPointerException if placer is null
     */
    public GameController(RandomShipPlacer placer) {
        this.placer = Objects.requireNonNull(placer, "Ship placer is required");
    }

    /** Starts a new game, discarding any game in progress. */
    public void newGame() {
        board = placer.createBoard();
        shotCount = 0;
        hitCount = 0;
    }

    /**
     * Reports whether a game is in progress.
     *
     * @return whether a game has been started
     */
    public boolean isGameStarted() {
        return board != null;
    }

    /**
     * Reports whether the current game has been won.
     *
     * @return whether every ship of the current game has been sunk
     */
    public boolean isGameOver() {
        return isGameStarted() && board.allShipsSunk();
    }

    /**
     * Fires at one cell of the current game.
     *
     * <p>A cell already fired at is reported as {@link ShotResult#ALREADY_SHOT} and
     * leaves every count unchanged, so a player cannot inflate their score by
     * clicking the same square twice. Once the fleet is destroyed the game is
     * closed and no further shot is accepted.
     *
     * @param cell cell chosen by the player
     * @return the outcome of the shot
     * @throws IllegalStateException if no game is in progress, or it is already won
     * @throws IllegalArgumentException if the cell is outside the board
     * @throws NullPointerException if cell is null
     */
    public ShotResult shoot(Coordinate cell) {
        requireGame();
        if (isGameOver()) {
            throw new IllegalStateException("The game is already won");
        }
        ShotResult result = board.fireAt(cell);
        if (result != ShotResult.ALREADY_SHOT) {
            shotCount++;
            if (result != ShotResult.MISS) {
                hitCount++;
            }
        }
        return result;
    }

    /**
     * Returns the size of the board the interface should draw.
     *
     * @return number of rows and columns on the current board
     * @throws IllegalStateException if no game is in progress
     */
    public int getBoardSize() {
        requireGame();
        return board.getSize();
    }

    /**
     * Returns how many shots the player has actually taken.
     *
     * @return number of shots that struck a new cell
     */
    public int getShotCount() {
        return shotCount;
    }

    /**
     * Returns how many of those shots found a ship.
     *
     * @return number of shots that struck a ship
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Returns how much of the fleet is still afloat.
     *
     * @return number of ships not yet sunk
     * @throws IllegalStateException if no game is in progress
     */
    public int getRemainingShips() {
        requireGame();
        return board.getShipCount() - board.getSunkShipCount();
    }

    /**
     * Returns everything the interface needs in order to colour the grid.
     *
     * @return an unmodifiable view of every cell already fired at, in shot order
     */
    public Map<Coordinate, ShotResult> getShots() {
        if (!isGameStarted()) {
            return Collections.emptyMap();
        }
        return board.getShots();
    }

    /**
     * Guards every operation that needs a game in progress.
     *
     * @throws IllegalStateException if no game has been started
     */
    private void requireGame() {
        if (!isGameStarted()) {
            throw new IllegalStateException("Start a game first");
        }
    }
}
