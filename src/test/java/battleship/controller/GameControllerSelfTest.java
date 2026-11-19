package battleship.controller;

import battleship.model.Coordinate;
import battleship.model.Difficulty;
import battleship.model.ShipStatus;
import battleship.model.ShotResult;
import battleship.service.RandomShipPlacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Dependency-free checks for the game lifecycle; this harness is not JUnit. */
public final class GameControllerSelfTest {
    private static int checks;

    public static void main(String[] args) {
        // Before a game is started there is nothing to shoot at.
        GameController fresh = new GameController();
        check(!fresh.isGameStarted(), "No game before newGame");
        check(!fresh.isGameOver(), "Unstarted game is not over");
        rejects(IllegalStateException.class, () -> fresh.shoot(c(0, 0)));

        GameController game = new GameController(new RandomShipPlacer(new Random(2026)));
        game.newGame();
        check(game.isGameStarted() && !game.isGameOver(), "Game starts unfinished");
        check(game.getBoardSize() == 10, "Standard board size");
        check(game.getShotCount() == 0 && game.getHitCount() == 0, "Counters start at zero");
        check(game.getRemainingShips() == 6, "Six ships afloat");
        check(game.getShots().isEmpty(), "No shots recorded yet");

        // A game started without a setting plays the board the brief describes.
        check(game.getDifficulty() == Difficulty.NORMAL, "Normal is the setting by default");
        check(game.getShotLimit() == Difficulty.NORMAL.getShotLimit(), "The limit comes from the setting");
        check(game.getShotsRemaining() == game.getShotLimit(), "Every shot is still available");
        check(!game.isWon() && !game.isLost(), "A new game is neither won nor lost");

        // The fleet is reported to the interface without any position in it.
        List<ShipStatus> fleet = game.getFleetStatus();
        check(fleet.size() == 6, "All six ships are reported");
        int reportedSquares = 0;
        for (ShipStatus ship : fleet) {
            check(!ship.isSunk(), "Nothing is sunk at the start");
            reportedSquares += ship.getLength();
        }
        check(reportedSquares == 16, "The reported fleet covers sixteen squares");

        // A shot at a fresh cell counts once, and a hit counts separately.
        ShotResult first = game.shoot(c(0, 0));
        check(game.getShotCount() == 1, "First shot counted");
        check(game.getHitCount() == (first == ShotResult.MISS ? 0 : 1), "Hits counted separately");
        check(game.getShots().size() == 1, "Shot recorded for the interface");

        // Firing at the same cell again must change nothing at all.
        check(game.shoot(c(0, 0)) == ShotResult.ALREADY_SHOT, "Duplicate reported");
        check(game.getShotCount() == 1, "Duplicate shot is not counted");
        check(game.getHitCount() == (first == ShotResult.MISS ? 0 : 1), "Duplicate leaves hits alone");

        // Invalid input is rejected and leaves the game untouched.
        rejects(IllegalArgumentException.class, () -> game.shoot(c(10, 0)));
        rejects(IllegalArgumentException.class, () -> game.shoot(c(-1, 0)));
        rejects(NullPointerException.class, () -> game.shoot(null));
        check(game.getShotCount() == 1, "Invalid shots change nothing");
        check(game.getShotsRemaining() == game.getShotLimit() - 1, "One shot has been used");

        // Play the whole board until the fleet is destroyed. Easy is used for this:
        // it is the only setting that allows a shot at every square, so working
        // through the grid is certain to finish the game rather than run out.
        game.newGame(Difficulty.EASY);
        check(game.getBoardSize() == Difficulty.EASY.getBoardSize(), "The setting chooses the board");
        for (int row = 0; row < game.getBoardSize() && !game.isGameOver(); row++) {
            for (int column = 0; column < game.getBoardSize() && !game.isGameOver(); column++) {
                if (!game.getShots().containsKey(c(row, column))) {
                    game.shoot(c(row, column));
                }
            }
        }
        check(game.isGameOver(), "Game ends when every ship is sunk");
        check(game.isWon() && !game.isLost(), "Sinking the fleet is a win, not a loss");
        check(game.getHitCount() == 16, "Sixteen cells held a ship");
        check(game.getRemainingShips() == 0, "No ships afloat");
        for (ShipStatus ship : game.getFleetStatus()) {
            check(ship.isSunk(), "Every reported ship is sunk after a win");
        }

        // The game is closed: no further shot of any kind is accepted.
        rejects(IllegalStateException.class, () -> game.shoot(c(0, 0)));
        int finalShots = game.getShotCount();
        rejects(IllegalStateException.class, () -> game.shoot(c(9, 9)));
        check(game.getShotCount() == finalShots, "Rejected shots after victory change nothing");

        // Restart clears the previous game completely.
        game.newGame();
        check(!game.isGameOver() && game.isGameStarted(), "Restart reopens the game");
        check(game.getShotCount() == 0 && game.getHitCount() == 0, "Restart clears counters");
        check(game.getShots().isEmpty() && game.getRemainingShips() == 6, "Restart clears the board");

        rejects(NullPointerException.class, () -> new GameController(null));
        rejects(NullPointerException.class, () -> game.newGame(null));
        rejects(UnsupportedOperationException.class, () -> game.getFleetStatus().clear());
        rejects(IllegalStateException.class, fresh::getDifficulty);
        rejects(IllegalStateException.class, fresh::getShotLimit);
        rejects(IllegalStateException.class, fresh::getShotsRemaining);
        check(fresh.getFleetStatus().isEmpty(), "An unstarted game reports no fleet");
        rejects(UnsupportedOperationException.class, () -> game.getShots().clear());
        rejects(IllegalStateException.class, fresh::getBoardSize);
        rejects(IllegalStateException.class, fresh::getRemainingShips);

        // A seeded generator makes a whole game reproducible, which is what allows
        // these checks to assert an exact hit count rather than a range.
        check(playAll(new Random(99)).equals(playAll(new Random(99))), "Same seed repeats the game");

        // The shot limit has to be able to end a game, or the setting means nothing.
        // Hard allows forty shots on sixty-four squares, so working through the grid
        // runs out for most fleets; a few seeds are tried so the check does not
        // depend on one lucky layout.
        boolean sawLoss = false;
        for (int seed = 1; seed <= 10; seed++) {
            GameController hard = new GameController(new RandomShipPlacer(new Random(seed)));
            hard.newGame(Difficulty.HARD);
            sweep(hard);
            check(hard.isGameOver(), "A swept game always ends");
            check(hard.isWon() != hard.isLost(), "A finished game is won or lost, never both");
            check(hard.getShotCount() <= hard.getShotLimit(), "The limit is never exceeded");
            if (hard.isLost()) {
                sawLoss = true;
                check(hard.getShotsRemaining() == 0, "A loss uses every shot");
                check(hard.getRemainingShips() > 0, "A loss leaves ships afloat");
                rejects(IllegalStateException.class, () -> hard.shoot(c(0, 0)));
            }
        }
        check(sawLoss, "Running out of shots really does end the game");

        System.out.println("PASS: " + checks + " GameController self-checks");
    }

    /** Fires at each cell in turn until the game ends, one way or the other. */
    private static void sweep(GameController game) {
        for (int row = 0; row < game.getBoardSize() && !game.isGameOver(); row++) {
            for (int column = 0; column < game.getBoardSize() && !game.isGameOver(); column++) {
                game.shoot(c(row, column));
            }
        }
    }

    /** Plays every cell of a seeded game in order and returns the outcome sequence. */
    private static List<ShotResult> playAll(Random random) {
        GameController game = new GameController(new RandomShipPlacer(random));
        game.newGame(Difficulty.EASY);
        List<ShotResult> outcomes = new ArrayList<>();
        for (int row = 0; row < game.getBoardSize() && !game.isGameOver(); row++) {
            for (int column = 0; column < game.getBoardSize() && !game.isGameOver(); column++) {
                outcomes.add(game.shoot(c(row, column)));
            }
        }
        return outcomes;
    }

    private static Coordinate c(int row, int column) { return new Coordinate(row, column); }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
        checks++;
    }

    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Unexpected exception", error);
            checks++;
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
