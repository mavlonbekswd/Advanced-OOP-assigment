package battleship.view;

import battleship.controller.GameController;
import battleship.model.Coordinate;
import battleship.model.Difficulty;
import battleship.model.ShotResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Objects;

/**
 * The window: a toolbar, the grid, the fleet panel beside it and a status line.
 *
 * <p>OOP: Architectural pattern. This is the view of the Model-View-Controller
 * split. It holds no rule about the game. A click becomes a request to the
 * controller, and the window then redraws itself from whatever the controller
 * reports. Because it decides nothing, a fault in the rules cannot hide in here.
 *
 * <p>OOP: Encapsulation. The window holds a controller and never reaches past it to
 * a board, so there is no path by which the interface could read, and accidentally
 * reveal, where the ships are hidden. The fleet list beside the grid is built from
 * conditions only, never from positions.
 *
 * <p>OOP: Interfaces and event handling. Buttons are given action listeners, and the
 * grid reports a click through its own small listener interface. The window supplies
 * the behaviour; Swing decides when to run it.
 *
 * <p>OOP: Composition. The window is assembled from a {@link BoardPanel} and a
 * {@link FleetPanel} rather than drawing everything itself, so each piece has one
 * job and can be changed without touching the other.
 */
public final class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    /** The only route from this window to the game; no board is reachable. */
    private final transient GameController controller;

    /** Lets the player pick a setting before, or instead of, a new game. */
    private final JComboBox<Difficulty> difficulties = new JComboBox<>(Difficulty.values());

    /** The line along the bottom describing the last shot. */
    private final JLabel status = new JLabel(" ");

    /** Keeps the grid's place in the layout while the grid itself is replaced. */
    private final JPanel boardHolder = new JPanel(new BorderLayout());

    /** The list of enemy ships shown beside the grid. */
    private final FleetPanel fleet = new FleetPanel();

    /** The current grid. A new one is built whenever the board size changes. */
    private BoardPanel board;

    /** The size the current grid was built for, so it is only rebuilt when it must be. */
    private int gridSize;

    /**
     * Builds the window and starts the first game.
     *
     * @param controller the controller this window sends every click to
     * @throws NullPointerException if controller is null
     */
    public GameWindow(GameController controller) {
        super("Battleship");
        this.controller = Objects.requireNonNull(controller, "Controller is required");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        // Fixed before the first pack: changing it afterwards can alter the frame's
        // borders and leave the window a few pixels out.
        setResizable(false);

        add(buildToolbar(), BorderLayout.NORTH);
        add(buildMiddle(), BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);

        startGame(Difficulty.NORMAL);
    }

    /** @return the row of controls across the top of the window */
    private JPanel buildToolbar() {
        difficulties.setSelectedItem(Difficulty.NORMAL);
        difficulties.addActionListener(event -> difficultyChosen());

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(event -> newGameRequested());

        JButton rules = new JButton("Rules");
        rules.addActionListener(event -> showRules());

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        bar.add(new JLabel("Difficulty:"));
        bar.add(difficulties);
        bar.add(newGame);
        bar.add(rules);
        return bar;
    }

    /** @return the grid with the fleet panel beside it */
    private JPanel buildMiddle() {
        JPanel middle = new JPanel(new BorderLayout(12, 0));
        middle.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        middle.add(boardHolder, BorderLayout.CENTER);
        middle.add(fleet, BorderLayout.EAST);
        return middle;
    }

    /** @return the line along the bottom that reports the last shot */
    private JPanel buildStatus() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
        panel.add(status, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Starts a game at the given setting.
     *
     * <p>The grid is only built again when the setting changes the number of
     * squares. Starting another game at the same setting reuses the squares
     * already on screen, so the window does not jump about.
     *
     * @param level the setting to play
     */
    private void startGame(Difficulty level) {
        controller.newGame(level);

        int size = controller.getBoardSize();
        boolean resized = board == null || gridSize != size;
        if (resized) {
            board = new BoardPanel(size, this::fireAt);
            gridSize = size;
            boardHolder.removeAll();
            boardHolder.add(board, BorderLayout.CENTER);
            boardHolder.revalidate();
        }

        setStatus("New game on " + level.getLabel() + ". Choose a square.");
        refresh();
        if (resized) {
            pack();
            // A different board means a different window, so put it back in the
            // middle of the screen rather than leaving it half off the edge.
            setLocationRelativeTo(null);
        }
    }

    /** Starts a new game at the setting currently chosen, after asking if needed. */
    private void newGameRequested() {
        if (confirmAbandon("Start a new game?")) {
            startGame(selectedDifficulty());
        }
    }

    /** Handles the player picking a different setting. */
    private void difficultyChosen() {
        Difficulty chosen = selectedDifficulty();
        if (chosen == controller.getDifficulty()) {
            return;
        }
        if (confirmAbandon("Changing the difficulty starts a new game. Continue?")) {
            startGame(chosen);
        } else {
            // Put the box back to the setting actually being played, or it would
            // show something different from the game on screen.
            difficulties.setSelectedItem(controller.getDifficulty());
        }
    }

    /**
     * Asks before throwing away a game that is under way.
     *
     * @param question what to ask the player
     * @return true if the current game may be abandoned
     */
    private boolean confirmAbandon(String question) {
        boolean underWay = controller.getShotCount() > 0 && !controller.isGameOver();
        if (!underWay) {
            return true;
        }
        int answer = JOptionPane.showConfirmDialog(this, question, "Battleship",
                JOptionPane.YES_NO_OPTION);
        return answer == JOptionPane.YES_OPTION;
    }

    /** @return whichever setting the combo box is showing */
    private Difficulty selectedDifficulty() {
        Difficulty chosen = (Difficulty) difficulties.getSelectedItem();
        return chosen == null ? Difficulty.NORMAL : chosen;
    }

    /**
     * Sends one shot to the controller and reports what happened.
     *
     * <p>A square already fired at is disabled, and the whole grid is disabled once
     * the game ends, so neither case can reach the controller from here.
     *
     * @param target the square the player clicked
     */
    private void fireAt(Coordinate target) {
        if (controller.isGameOver()) {
            return;
        }
        ShotResult result = controller.shoot(target);
        refresh();

        if (controller.isWon()) {
            setStatus("Every ship sunk in " + controller.getShotCount() + " shots.");
            JOptionPane.showMessageDialog(this,
                    "You sank the whole fleet in " + controller.getShotCount() + " shots.",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
        } else if (controller.isLost()) {
            setStatus("Out of shots with " + controller.getRemainingShips() + " ship(s) afloat.");
            JOptionPane.showMessageDialog(this,
                    "You have used all " + controller.getShotLimit() + " shots and "
                            + controller.getRemainingShips() + " ship(s) survived.",
                    "Out of shots", JOptionPane.WARNING_MESSAGE);
        } else {
            setStatus(describe(target, result));
        }
    }

    /**
     * Puts one shot into words for the status line.
     *
     * @param target the square fired at
     * @param result what the shot turned out to be
     * @return the sentence to show
     */
    private String describe(Coordinate target, ShotResult result) {
        // The dash is written as an escape so that the file stays plain ASCII and
        // compiles the same way whatever character set the compiler defaults to.
        String square = BoardPanel.nameOf(target) + " \u2014 ";
        switch (result) {
            case MISS: return square + "miss.";
            case HIT: return square + "hit.";
            case SUNK: return square + "hit, and that ship is sunk.";
            default: return square + "already fired at.";
        }
    }

    /** Redraws the grid and the fleet panel from the controller's state. */
    private void refresh() {
        board.update(controller.getShots(), controller.isGameOver());
        fleet.update(controller.getFleetStatus(), controller.getShotCount(),
                controller.getShotLimit(), controller.getHitCount());
    }

    /**
     * Shows the text on the status line.
     *
     * @param text the sentence to show
     */
    private void setStatus(String text) {
        status.setText(text);
    }

    /** Explains the game, for a player who has not met this version of it. */
    private void showRules() {
        JOptionPane.showMessageDialog(this,
                "The grid hides one Battleship of four squares, two Cruisers of three\n"
                + "and three Destroyers of two.\n\n"
                + "No two ships overlap, and no two touch, not even at a corner.\n\n"
                + "A square you fire at turns blue for a miss and red for a hit;\n"
                + "a ship that has been completely destroyed turns a darker red.\n\n"
                + "Sink every ship before your shots run out. Easy allows one shot\n"
                + "per square, so only Normal and Hard can be lost.",
                "How to play", JOptionPane.INFORMATION_MESSAGE);
    }
}
