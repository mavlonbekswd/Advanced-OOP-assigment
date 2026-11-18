package battleship.view;

import battleship.controller.GameController;
import battleship.model.Coordinate;
import battleship.model.ShotResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Objects;

/**
 * The Swing window: a clickable grid, a status line and a New Game button.
 *
 * <p>OOP: Architectural pattern. This is the view of the Model-View-Controller
 * split. It contains no rule about the game. It turns a click into a request to the
 * controller, then redraws itself from whatever the controller reports. Because it
 * decides nothing, a fault in the rules can never hide in here.
 *
 * <p>OOP: Encapsulation. The window holds a controller but never reaches past it to
 * a board, so there is no path by which the interface could read, and accidentally
 * reveal, where the ships are hidden.
 *
 * <p>OOP: Interfaces and event handling. Each cell is given an action listener,
 * which is Swing's callback interface; the window supplies the behaviour and Swing
 * decides when to run it.
 */
public final class GameWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Pixel size of one cell of the grid. */
    private static final int CELL_SIZE = 42;

    /** The only route from this window to the game; no board is reachable. */
    private final transient GameController controller;

    /** The grid buttons, held by row and column so they can be redrawn. */
    private final JButton[][] cells;
    /** The running summary shown above the grid. */
    private final JLabel status = new JLabel("", SwingConstants.CENTER);

    /**
     * Builds the window and starts the first game.
     *
     * @param controller the controller this window sends every click to
     * @throws NullPointerException if controller is null
     */
    public GameWindow(GameController controller) {
        super("Battleship");
        this.controller = Objects.requireNonNull(controller, "Controller is required");
        this.controller.newGame();
        this.cells = new JButton[this.controller.getBoardSize()][this.controller.getBoardSize()];

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(buildStatus(), BorderLayout.NORTH);
        add(buildGrid(), BorderLayout.CENTER);
        add(buildControls(), BorderLayout.SOUTH);
        refresh();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /** @return the status line shown above the grid */
    private JPanel buildStatus() {
        status.setFont(status.getFont().deriveFont(Font.BOLD, 14f));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panel.add(status, BorderLayout.CENTER);
        return panel;
    }

    /** @return the grid of clickable cells */
    private JPanel buildGrid() {
        int size = controller.getBoardSize();
        JPanel grid = new JPanel(new GridLayout(size, size, 2, 2));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                JButton cell = buildCell(row, column);
                cells[row][column] = cell;
                grid.add(cell);
            }
        }
        return grid;
    }

    /**
     * Builds one cell and connects it to the controller.
     *
     * @param row zero-based row of this cell
     * @param column zero-based column of this cell
     * @return the configured button
     */
    private JButton buildCell(int row, int column) {
        JButton cell = new JButton();
        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cell.setFont(cell.getFont().deriveFont(Font.BOLD, 16f));
        // A button keeps a wide default margin, which left so little room inside a
        // cell this small that the single-character mark was clipped to an ellipsis.
        cell.setMargin(new Insets(0, 0, 0, 0));
        cell.setFocusPainted(false);
        cell.setOpaque(true);
        cell.setBorderPainted(false);
        cell.addActionListener(event -> fireAt(new Coordinate(row, column)));
        return cell;
    }

    /** @return the panel holding the New Game button */
    private JPanel buildControls() {
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(event -> {
            controller.newGame();
            refresh();
        });
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.add(newGame);
        return panel;
    }

    /**
     * Sends one shot to the controller and redraws the result.
     *
     * <p>A click on a cell already fired at is simply ignored, and a click after the
     * game has been won cannot happen because the grid is disabled at that point.
     *
     * @param target the cell the player clicked
     */
    private void fireAt(Coordinate target) {
        if (controller.isGameOver()) {
            return;
        }
        ShotResult result = controller.shoot(target);
        refresh();
        if (controller.isGameOver()) {
            announceVictory();
        } else if (result == ShotResult.SUNK) {
            status.setText(statusText() + "  —  Sunk!");
        }
    }

    /** Redraws every cell and the status line from the controller's state. */
    private void refresh() {
        Map<Coordinate, ShotResult> shots = controller.getShots();
        boolean over = controller.isGameOver();
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                ShotResult result = shots.get(new Coordinate(row, column));
                JButton cell = cells[row][column];
                cell.setBackground(CellStyle.colourFor(result));
                cell.setText(CellStyle.labelFor(result));
                cell.setEnabled(!over && result == null);
            }
        }
        status.setText(statusText());
    }

    /** @return the running summary shown above the grid */
    private String statusText() {
        if (controller.isGameOver()) {
            return "All ships sunk in " + controller.getShotCount() + " shots";
        }
        return "Shots: " + controller.getShotCount()
                + "    Hits: " + controller.getHitCount()
                + "    Ships left: " + controller.getRemainingShips();
    }

    /** Tells the player they have won. */
    private void announceVictory() {
        JOptionPane.showMessageDialog(this,
                "You sank the whole fleet in " + controller.getShotCount() + " shots.",
                "Victory",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
