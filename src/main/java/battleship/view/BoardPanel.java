package battleship.view;

import battleship.model.Coordinate;
import battleship.model.ShotResult;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Objects;

/**
 * The grid the player clicks on, with a letter above each column and a number
 * beside each row.
 *
 * <p>The labels are not decoration. Without them a player describing a square has
 * to count cells, and the fleet shown at the end of a game cannot be read back
 * against anything. They are generated from the size of the board, so they follow
 * the chosen difficulty on their own.
 *
 * <p>OOP: Interfaces and event handling. The panel knows nothing about the game. It
 * reports a click through {@link ShotListener}, and whoever built it decides what a
 * click means. That is what keeps the rules out of the drawing code.
 *
 * <p>OOP: Encapsulation. The buttons are private. The only way to change what the
 * grid shows is {@link #update(Map, boolean)}, so no other class can put the grid
 * into a state the game never reached.
 */
public final class BoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Pixel size of one square. */
    private static final int CELL_SIZE = 42;

    /** Width of the column of row numbers down the left-hand side. */
    private static final int AXIS_WIDTH = 22;

    /** Space left between the labels and the grid. */
    private static final int GAP = 4;

    /** Told when the player clicks a square. */
    public interface ShotListener {
        /**
         * Reports that a square was clicked.
         *
         * @param cell the square the player chose
         */
        void shotFired(Coordinate cell);
    }

    /** Rows and columns on this grid. */
    private final int size;

    /** Every square, held by row and column so it can be redrawn. */
    private final JButton[][] cells;

    /**
     * Builds a grid of the given size.
     *
     * @param size rows and columns to draw
     * @param listener told whenever the player clicks a square
     * @throws IllegalArgumentException if size is not positive
     * @throws NullPointerException if listener is null
     */
    public BoardPanel(int size, ShotListener listener) {
        super(new BorderLayout(GAP, GAP));
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        Objects.requireNonNull(listener, "Shot listener is required");
        this.size = size;
        this.cells = new JButton[size][size];

        // A square is disabled the moment it has been fired at, and a disabled
        // button draws its text in the look and feel's own grey. Against the red of
        // a hit that grey falls well under the contrast a player can rely on, and it
        // would apply to every mark on the board. This is the only way the look and
        // feel will take another colour; it is a default rather than state of this
        // panel, so setting it again when the grid is rebuilt changes nothing.
        UIManager.put("Button.disabledText", CellStyle.markColour());

        add(buildColumnLabels(), BorderLayout.NORTH);
        add(buildRowLabels(), BorderLayout.WEST);
        add(buildGrid(listener), BorderLayout.CENTER);
    }

    /** @return the row of letters that sits above the grid */
    private JPanel buildColumnLabels() {
        JPanel letters = new JPanel(new GridLayout(1, size, 2, 0));
        for (int column = 0; column < size; column++) {
            letters.add(axisLabel(String.valueOf((char) ('A' + column))));
        }
        // An empty corner keeps the letters lined up with the squares below them,
        // because the row numbers take up space on the left. The gap has to match
        // the one this panel's own layout leaves beside the row numbers, or every
        // letter sits a few pixels to the left of its column.
        JPanel row = new JPanel(new BorderLayout(GAP, 0));
        row.add(corner(), BorderLayout.WEST);
        row.add(letters, BorderLayout.CENTER);
        return row;
    }

    /** @return the column of numbers that sits beside the grid */
    private JPanel buildRowLabels() {
        JPanel numbers = new JPanel(new GridLayout(size, 1, 0, 2));
        numbers.setPreferredSize(new Dimension(AXIS_WIDTH, 0));
        for (int row = 0; row < size; row++) {
            numbers.add(axisLabel(String.valueOf(row + 1)));
        }
        return numbers;
    }

    /**
     * Builds the squares themselves.
     *
     * @param listener told whenever a square is clicked
     * @return the panel holding every square
     */
    private JPanel buildGrid(ShotListener listener) {
        JPanel grid = new JPanel(new GridLayout(size, size, 2, 2));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Coordinate cell = new Coordinate(row, column);
                JButton button = buildCell(cell);
                button.addActionListener(event -> listener.shotFired(cell));
                cells[row][column] = button;
                grid.add(button);
            }
        }
        return grid;
    }

    /**
     * Builds one square.
     *
     * @param cell the position this square stands for
     * @return the configured button
     */
    private JButton buildCell(Coordinate cell) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 15f));
        // A button keeps a wide default margin. At this size it left so little room
        // that the single character was clipped to an ellipsis on screen.
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setToolTipText(nameOf(cell));
        return button;
    }

    /**
     * Repaints the grid from what the player has been told so far.
     *
     * @param shots every square already fired at, and what it turned out to be
     * @param locked true when the game has finished and clicks should do nothing
     * @throws NullPointerException if shots is null
     */
    public void update(Map<Coordinate, ShotResult> shots, boolean locked) {
        Objects.requireNonNull(shots, "Shots are required");
        // Asked once rather than per square: every square carries the same font, and
        // the answer cannot change while the grid is being redrawn.
        CellStyle.Marks marks = CellStyle.marksFor(cells[0][0].getFont());
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                ShotResult result = shots.get(new Coordinate(row, column));
                JButton button = cells[row][column];
                button.setBackground(CellStyle.colourFor(result));
                button.setText(CellStyle.labelFor(result, marks));
                button.setEnabled(!locked && result == null);
            }
        }
    }

    /**
     * Returns the name a player would use for a square, such as D4.
     *
     * @param cell the square to name
     * @return the letter of its column followed by the number of its row
     */
    public static String nameOf(Coordinate cell) {
        Objects.requireNonNull(cell, "Cell is required");
        return (char) ('A' + cell.getColumn()) + String.valueOf(cell.getRow() + 1);
    }

    /**
     * Builds one of the small labels around the edge of the grid.
     *
     * @param text the letter or number to show
     * @return a centred label, smaller than the text on the squares
     */
    private static JLabel axisLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        return label;
    }

    /**
     * Builds the empty square where the two sets of labels meet.
     *
     * @return a blank spacer as wide as the column of row numbers
     */
    private static JPanel corner() {
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(AXIS_WIDTH, 0));
        spacer.setOpaque(false);
        return spacer;
    }
}
