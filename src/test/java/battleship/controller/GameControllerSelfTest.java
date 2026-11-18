package battleship.controller;

import battleship.model.Coordinate;
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

        // Play the whole board until the fleet is destroyed.
        for (int row = 0; row < game.getBoardSize() && !game.isGameOver(); row++) {
            for (int column = 0; column < game.getBoardSize() && !game.isGameOver(); column++) {
                if (!game.getShots().containsKey(c(row, column))) {
                    game.shoot(c(row, column));
                }
            }
        }
        check(game.isGameOver(), "Game ends when every ship is sunk");
        check(game.getHitCount() == 16, "Sixteen cells held a ship");
        check(game.getRemainingShips() == 0, "No ships afloat");

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
        rejects(UnsupportedOperationException.class, () -> game.getShots().clear());
        rejects(IllegalStateException.class, fresh::getBoardSize);
        rejects(IllegalStateException.class, fresh::getRemainingShips);

        // A seeded generator makes a whole game reproducible, which is what allows
        // these checks to assert an exact hit count rather than a range.
        check(playAll(new Random(99)).equals(playAll(new Random(99))), "Same seed repeats the game");

        System.out.println("PASS: " + checks + " GameController self-checks");
    }

    /** Plays every cell of a seeded game in order and returns the outcome sequence. */
    private static List<ShotResult> playAll(Random random) {
        GameController game = new GameController(new RandomShipPlacer(random));
        game.newGame();
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
