package battleship.model;

/**
 * Dependency-free checks for the difficulty settings; this harness is not JUnit.
 *
 * <p>The settings are plain data, but they carry the only promise the rest of the
 * game relies on: that a harder setting really is harder, and that the easiest one
 * cannot run out of shots.
 */
public final class DifficultySelfTest {
    private static int checks;

    private DifficultySelfTest() {
        // Test harness: no instance is needed.
    }

    /**
     * Runs every check and reports how many passed.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // The brief asks for levels that differ in board size and in shots allowed.
        check(Difficulty.values().length == 3, "Three settings are offered");
        check(Difficulty.EASY.getBoardSize() > Difficulty.NORMAL.getBoardSize(),
                "Easy has a larger board than Normal");
        check(Difficulty.NORMAL.getBoardSize() > Difficulty.HARD.getBoardSize(),
                "Normal has a larger board than Hard");

        // The standard game the brief describes is ten by ten, so that is Normal.
        check(Difficulty.NORMAL.getBoardSize() == Board.DEFAULT_SIZE,
                "Normal is the board size the brief fixes");

        for (Difficulty level : Difficulty.values()) {
            check(level.getBoardSize() > 0, level + " has a positive board size");
            check(level.getShotLimit() > 0, level + " allows at least one shot");
            check(level.getShotLimit() <= level.getBoardSize() * level.getBoardSize(),
                    level + " never allows more shots than there are squares");
            check(level.getLabel() != null && !level.getLabel().isEmpty(),
                    level + " has a label for the interface");
            check(level.toString().equals(level.getLabel()),
                    level + " prints as its label, so a combo box needs no renderer");
        }

        // Easy is meant to be forgiving: every square may be fired at, so a player
        // who simply works through the grid will always finish the game.
        int easySquares = Difficulty.EASY.getBoardSize() * Difficulty.EASY.getBoardSize();
        check(Difficulty.EASY.getShotLimit() == easySquares, "Easy cannot run out of shots");

        // The other two have to bite, or the setting would mean nothing.
        check(Difficulty.NORMAL.getShotLimit()
                < Difficulty.NORMAL.getBoardSize() * Difficulty.NORMAL.getBoardSize(),
                "Normal limits the player");
        check(Difficulty.HARD.getShotLimit()
                < Difficulty.NORMAL.getShotLimit(), "Hard is tighter than Normal");

        // The fleet in the brief is one Battleship, two Cruisers and three
        // Destroyers, so sixteen squares have to be hit. A level that allowed fewer
        // shots than that could never be won at all.
        int fleetSquares = ShipType.BATTLESHIP.getLength()
                + 2 * ShipType.CRUISER.getLength()
                + 3 * ShipType.DESTROYER.getLength();
        check(fleetSquares == 16, "The brief's fleet covers sixteen squares");
        for (Difficulty level : Difficulty.values()) {
            check(level.getShotLimit() > fleetSquares,
                    level + " allows more shots than the fleet has squares");
        }

        System.out.println("PASS: " + checks + " Difficulty self-checks");
    }

    private static void check(boolean result, String message) {
        if (!result) {
            throw new AssertionError(message);
        }
        checks++;
    }
}
