package battleship.model;

/**
 * The three settings a player can choose between before a game starts.
 *
 * <p>The brief allows a harder game to differ in board size and in the number of
 * shots allowed, and both are used here. The fleet itself is never changed: the
 * assignment fixes one Battleship, two Cruisers and three Destroyers, so a smaller
 * board is harder because the same sixteen squares have to be found with fewer
 * shots and from a tighter grid.
 *
 * <p>OOP: Abstraction. A setting is named by what it means to the player, not by the
 * two numbers behind it, so the rest of the program asks for "the board size of this
 * level" and never repeats the numbers themselves.
 *
 * <p>Easy deliberately allows one shot per square. A player who simply works along
 * the grid will therefore always finish the game, which is what makes it the easy
 * setting; the limit only starts to bite on Normal and Hard.
 */
public enum Difficulty {

    /** A larger board, and as many shots as there are squares: losing is impossible. */
    EASY("Easy", 12, 144),

    /** The ten by ten game described in the brief, with a limit that can be reached. */
    NORMAL("Normal", 10, 70),

    /** A crowded board and few shots, so the fleet has to be hunted rather than swept. */
    HARD("Hard", 8, 40);

    private final String label;
    private final int boardSize;
    private final int shotLimit;

    Difficulty(String label, int boardSize, int shotLimit) {
        this.label = label;
        this.boardSize = boardSize;
        this.shotLimit = shotLimit;
    }

    /**
     * Returns the name shown to the player.
     *
     * @return the wording used in the interface
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns how many rows and columns this setting plays on.
     *
     * @return the length of one side of the board
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Returns how many shots the player is given.
     *
     * @return the largest number of shots that may be taken
     */
    public int getShotLimit() {
        return shotLimit;
    }

    /**
     * Returns the label, so a combo box can show the settings without a renderer.
     *
     * @return the name shown to the player
     */
    @Override
    public String toString() {
        return label;
    }
}
