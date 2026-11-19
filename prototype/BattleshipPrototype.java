import battleship.model.Board;
import battleship.model.Coordinate;
import battleship.model.ShipType;
import battleship.model.ShotResult;
import battleship.service.RandomShipPlacer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * THROWAWAY PROTOTYPE — not part of the submission.
 *
 * <p>This file exists to try out a new look for the game before any of it is
 * written into {@code src/main/java}. Nothing under {@code src} is modified and
 * nothing in {@code docs} is touched: the existing application still builds, still
 * runs and still matches the report exactly as it did.
 *
 * <p>It drives the real {@link Board} and the real {@link RandomShipPlacer}, so what
 * is shown is the actual game, not a fake. Two things are deliberately done here
 * that the submitted code does not do yet, so that they can be judged before being
 * committed to:
 *
 * <ul>
 *   <li>A difficulty setting: board size and a limit on the number of shots. The
 *       shot budget is counted in {@code Session} below, which is a sketch of what
 *       {@code GameController} would have to grow.</li>
 *   <li>A fleet panel showing which ships have been sunk. It learns this through
 *       the public API only: when a shot answers {@code SUNK}, the cells touching
 *       it that have been hit are collected, and their number is the length of the
 *       ship that has just gone down. The board is never asked where its ships are,
 *       so hiding them is still complete.</li>
 * </ul>
 *
 * <p>Run it with {@code ./prototype/run.sh}.
 */
public final class BattleshipPrototype extends JFrame {

    // The palette from the design mock-up.
    private static final Color INK = new Color(0x26, 0x34, 0x45);
    private static final Color HEADER = new Color(0x34, 0x49, 0x5E);
    private static final Color MUTED = new Color(0x65, 0x73, 0x83);
    private static final Color PANEL_BG = new Color(0xF7, 0xF9, 0xFB);
    private static final Color PANEL_LINE = new Color(0xD4, 0xDC, 0xE4);
    private static final Color STATUS_BG = new Color(0xF1, 0xF5, 0xF8);

    private static final Color WATER = new Color(0xE2, 0xEA, 0xF2);
    private static final Color WATER_EDGE = new Color(0xAE, 0xBD, 0xCC);
    private static final Color WATER_HOVER = new Color(0xC7, 0xD9, 0xE9);
    private static final Color MISS = new Color(0x55, 0x96, 0xD0);
    private static final Color MISS_EDGE = new Color(0x39, 0x7D, 0xB9);
    private static final Color HIT = new Color(0xDF, 0x62, 0x62);
    private static final Color HIT_EDGE = new Color(0xBB, 0x46, 0x46);
    private static final Color BLOCK = new Color(0x83, 0x95, 0xA6);
    private static final Color BLOCK_EDGE = new Color(0x6B, 0x7E, 0x90);

    private static final String FAMILY = "Arial";
    private static final Font BODY = new Font(FAMILY, Font.PLAIN, 12);
    private static final Font BOLD = new Font(FAMILY, Font.BOLD, 12);

    /** The fleet the brief fixes, with the names shown in the side panel. */
    private static final String[] SHIP_NAMES = {
        "Battleship", "Cruiser 1", "Cruiser 2", "Destroyer 1", "Destroyer 2", "Destroyer 3",
    };
    private static final ShipType[] SHIP_TYPES = {
        ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.CRUISER,
        ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
    };

    /** Board size and shot budget for each setting. */
    private enum Difficulty {
        EASY("Easy", 12, 110), NORMAL("Normal", 10, 60), HARD("Hard", 8, 30);

        private final String label;
        private final int boardSize;
        private final int shotLimit;

        Difficulty(String label, int boardSize, int shotLimit) {
            this.label = label;
            this.boardSize = boardSize;
            this.shotLimit = shotLimit;
        }

        @Override
        public String toString() {
            return label + "  (" + boardSize + "×" + boardSize + ", " + shotLimit + " shots)";
        }
    }

    /**
     * One game in progress. This is the part that would move into
     * {@code GameController}: it owns the board, counts the shots and decides when
     * the budget has run out.
     */
    private static final class Session {
        private final Board board;
        private final int shotLimit;
        private int shots;
        private int hits;

        Session(Difficulty difficulty) {
            this.board = new RandomShipPlacer().createBoard(difficulty.boardSize);
            this.shotLimit = difficulty.shotLimit;
        }

        ShotResult fireAt(Coordinate cell) {
            ShotResult result = board.fireAt(cell);
            if (result != ShotResult.ALREADY_SHOT) {
                shots++;
                if (result != ShotResult.MISS) {
                    hits++;
                }
            }
            return result;
        }

        boolean won() {
            return board.allShipsSunk();
        }

        boolean lost() {
            return !won() && shots >= shotLimit;
        }

        boolean over() {
            return won() || lost();
        }
    }

    private Difficulty difficulty = Difficulty.NORMAL;
    private Session session;

    private final JPanel boardHolder = new JPanel(new BorderLayout(8, 0));
    private final JPanel fleetPanel = new JPanel();
    private final JLabel shotsValue = new JLabel();
    private final JLabel hitsValue = new JLabel();
    private final JLabel sunkValue = new JLabel();
    private final JLabel status = new JLabel();
    private final JComboBox<Difficulty> difficultyBox = new JComboBox<>(Difficulty.values());

    private final Map<Coordinate, ShotResult> seen = new LinkedHashMap<>();
    private final List<Boolean> sunkFlags = new ArrayList<>();
    private CellButton[][] cells;

    private BattleshipPrototype() {
        super("Battleship Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        add(header(), BorderLayout.NORTH);
        add(content(), BorderLayout.CENTER);

        startGame();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    /* ---------------- the dark bar across the top ---------------- */

    private JComponent header() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(HEADER);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 12));

        JLabel name = new JLabel("Battleship Game");
        name.setFont(new Font(FAMILY, Font.BOLD, 14));
        name.setForeground(Color.WHITE);
        bar.add(name, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JLabel caption = new JLabel("Difficulty:");
        caption.setFont(BODY);
        caption.setForeground(new Color(0xD8, 0xE0, 0xE8));
        difficultyBox.setFont(BODY);
        difficultyBox.setSelectedItem(difficulty);
        difficultyBox.addActionListener(event -> changeDifficulty());
        right.add(caption);
        right.add(difficultyBox);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    /* ---------------- everything below the bar ---------------- */

    private JComponent content() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        JLabel title = new JLabel("Battleship");
        title.setFont(new Font(FAMILY, Font.BOLD, 24));
        title.setForeground(INK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel("Click a square to fire at the hidden fleet.");
        subtitle.setFont(new Font(FAMILY, Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);
        panel.add(heading, BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(24, 0));
        middle.setOpaque(false);
        boardHolder.setOpaque(false);
        middle.add(boardHolder, BorderLayout.CENTER);
        middle.add(sidePanel(), BorderLayout.EAST);
        panel.add(middle, BorderLayout.CENTER);

        panel.add(south(), BorderLayout.SOUTH);
        return panel;
    }

    private JComponent sidePanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(PANEL_BG);
        side.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_LINE),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        side.setPreferredSize(new Dimension(190, 10));

        JLabel heading = new JLabel("Enemy fleet");
        heading.setFont(new Font(FAMILY, Font.BOLD, 13));
        heading.setForeground(INK);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(heading);
        side.add(Box.createVerticalStrut(14));

        fleetPanel.setLayout(new BoxLayout(fleetPanel, BoxLayout.Y_AXIS));
        fleetPanel.setOpaque(false);
        fleetPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(fleetPanel);

        side.add(Box.createVerticalStrut(14));
        JPanel rule = new JPanel();
        rule.setBackground(PANEL_LINE);
        rule.setPreferredSize(new Dimension(10, 1));
        rule.setMinimumSize(new Dimension(10, 1));
        rule.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        rule.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(rule);
        side.add(Box.createVerticalStrut(10));

        side.add(statRow("Shots:", shotsValue));
        side.add(statRow("Hits:", hitsValue));
        side.add(statRow("Sunk:", sunkValue));
        side.add(Box.createVerticalGlue());
        return side;
    }

    private JComponent statRow(String caption, JLabel value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel left = new JLabel(caption);
        left.setFont(BODY);
        left.setForeground(MUTED);
        value.setFont(BOLD);
        value.setForeground(INK);
        value.setHorizontalAlignment(JLabel.RIGHT);
        row.add(left, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);
        return row;
    }

    private JComponent south() {
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setOpaque(false);

        south.add(legend());
        south.add(Box.createVerticalStrut(14));

        status.setFont(new Font(FAMILY, Font.PLAIN, 13));
        status.setForeground(INK);
        status.setOpaque(true);
        status.setBackground(STATUS_BG);
        status.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PANEL_LINE),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        status.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        south.add(status);
        south.add(Box.createVerticalStrut(12));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.add(flatButton("New game", true, event -> newGameRequested()));
        actions.add(flatButton("Rules", false, event -> showRules()));
        south.add(actions);
        return south;
    }

    private JComponent legend() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(legendItem(WATER, WATER_EDGE, "Unexplored"));
        row.add(legendItem(HIT, HIT_EDGE, "Hit"));
        row.add(legendItem(MISS, MISS_EDGE, "Miss"));
        return row;
    }

    private JComponent legendItem(Color fill, Color edge, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        item.add(new Swatch(fill, edge));
        JLabel label = new JLabel(text);
        label.setFont(BODY);
        label.setForeground(MUTED);
        item.add(label);
        return item;
    }

    private JButton flatButton(String text, boolean primary, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font(FAMILY, primary ? Font.BOLD : Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? HEADER : new Color(0xA6, 0xB3, 0xC0)),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        button.setBackground(primary ? HEADER : new Color(0xF3, 0xF5, 0xF7));
        button.setForeground(primary ? Color.WHITE : INK);
        button.setOpaque(true);
        button.addActionListener(action);
        return button;
    }

    /* ---------------- the grid ---------------- */

    private void buildGrid(int size) {
        boardHolder.removeAll();
        cells = new CellButton[size][size];

        JPanel columns = new JPanel(new GridLayout(1, size, 4, 0));
        columns.setOpaque(false);
        for (int column = 0; column < size; column++) {
            columns.add(axisLabel(String.valueOf((char) ('A' + column))));
        }
        JPanel columnsRow = new JPanel(new BorderLayout());
        columnsRow.setOpaque(false);
        columnsRow.add(Box.createHorizontalStrut(22), BorderLayout.WEST);
        columnsRow.add(columns, BorderLayout.CENTER);
        columnsRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JPanel rows = new JPanel(new GridLayout(size, 1, 0, 4));
        rows.setOpaque(false);
        rows.setPreferredSize(new Dimension(22, 10));
        for (int row = 0; row < size; row++) {
            rows.add(axisLabel(String.valueOf(row + 1)));
        }

        JPanel grid = new JPanel(new GridLayout(size, size, 4, 4));
        grid.setOpaque(false);
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                CellButton cell = new CellButton(new Coordinate(row, column));
                cell.addActionListener(event -> fire(cell));
                cells[row][column] = cell;
                grid.add(cell);
            }
        }

        boardHolder.add(columnsRow, BorderLayout.NORTH);
        boardHolder.add(rows, BorderLayout.WEST);
        boardHolder.add(grid, BorderLayout.CENTER);
        boardHolder.revalidate();
        boardHolder.repaint();
    }

    private JLabel axisLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(BODY);
        label.setForeground(MUTED);
        return label;
    }

    /** A grid square. It is still a JButton, so it still raises an action event. */
    private final class CellButton extends JButton {
        private final Coordinate cell;

        CellButton(Coordinate cell) {
            this.cell = cell;
            setPreferredSize(new Dimension(34, 34));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setToolTipText(name());
        }

        private String name() {
            return (char) ('A' + cell.getColumn()) + String.valueOf(cell.getRow() + 1);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            ShotResult result = seen.get(cell);
            Color fill = WATER;
            Color edge = WATER_EDGE;
            if (result == ShotResult.MISS) {
                fill = MISS;
                edge = MISS_EDGE;
            } else if (result != null) {
                fill = HIT;
                edge = HIT_EDGE;
            } else if (getModel().isRollover() && isEnabled()) {
                fill = WATER_HOVER;
            }

            g.setColor(fill);
            g.fillRoundRect(0, 0, w - 1, h - 1, 4, 4);
            g.setColor(edge);
            g.drawRoundRect(0, 0, w - 1, h - 1, 4, 4);

            // The marks are drawn rather than written. An earlier version used text
            // and the look and feel's font had no glyph for it, so every cell showed
            // an empty box instead.
            if (result == ShotResult.MISS) {
                g.setColor(Color.WHITE);
                int d = Math.max(4, w / 5);
                g.fillOval((w - d) / 2, (h - d) / 2, d, d);
            } else if (result != null) {
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int pad = Math.max(8, w / 3);
                g.drawLine(pad, pad, w - pad, h - pad);
                g.drawLine(w - pad, pad, pad, h - pad);
            }
            g.dispose();
        }
    }

    /** The little colour key used by the legend and by the fleet blocks. */
    private static class Swatch extends JComponent {
        private final Color fill;
        private final Color edge;

        Swatch(Color fill, Color edge) {
            this(fill, edge, 13, 13);
        }

        Swatch(Color fill, Color edge, int width, int height) {
            this.fill = fill;
            this.edge = edge;
            setPreferredSize(new Dimension(width, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(fill);
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.setColor(edge);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /** One row of the fleet panel: the ship's name and a block per cell. */
    private static final class FleetRow extends JPanel {
        FleetRow(String name, int length, boolean sunk) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

            JPanel head = new JPanel(new BorderLayout());
            head.setOpaque(false);
            head.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel label = new JLabel(name);
            label.setFont(BODY);
            label.setForeground(sunk ? new Color(0xB7, 0x36, 0x36) : INK);
            JLabel state = new JLabel(sunk ? "Sunk" : "");
            state.setFont(BODY);
            state.setForeground(new Color(0xB7, 0x36, 0x36));
            head.add(label, BorderLayout.WEST);
            head.add(state, BorderLayout.EAST);
            add(head);
            add(Box.createVerticalStrut(4));

            JPanel blocks = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
            blocks.setOpaque(false);
            blocks.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (int i = 0; i < length; i++) {
                blocks.add(new Swatch(sunk ? HIT : BLOCK, sunk ? HIT_EDGE : BLOCK_EDGE, 18, 10));
            }
            add(blocks);
        }
    }

    /* ---------------- playing ---------------- */

    private void startGame() {
        session = new Session(difficulty);
        seen.clear();
        sunkFlags.clear();
        for (int i = 0; i < SHIP_TYPES.length; i++) {
            sunkFlags.add(Boolean.FALSE);
        }
        buildGrid(difficulty.boardSize);
        setStatus("New game started. Pick a square.");
        refresh();
        pack();
    }

    private void newGameRequested() {
        if (session.shots > 0 && !session.over()) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "Restart the game in progress?", "New game", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }
        startGame();
    }

    private void changeDifficulty() {
        Difficulty chosen = (Difficulty) difficultyBox.getSelectedItem();
        if (chosen == null || chosen == difficulty) {
            return;
        }
        if (session != null && session.shots > 0 && !session.over()) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "Changing the difficulty starts a new game. Continue?",
                    "Difficulty", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                difficultyBox.setSelectedItem(difficulty);
                return;
            }
        }
        difficulty = chosen;
        startGame();
    }

    private void fire(CellButton button) {
        if (session.over()) {
            return;
        }
        Coordinate cell = button.cell;
        ShotResult result = session.fireAt(cell);
        if (result == ShotResult.ALREADY_SHOT) {
            return;
        }
        seen.put(cell, result);
        button.setEnabled(false);

        String name = button.name();
        if (result == ShotResult.MISS) {
            setStatus(name + " — miss.");
        } else if (result == ShotResult.HIT) {
            setStatus(name + " — hit!");
        } else {
            int length = markSunk(cell);
            setStatus(name + " — hit! A ship of " + length + " squares has been sunk.");
        }
        refresh();
        finishIfOver();
    }

    /**
     * Works out which ship has just gone down, without asking the board.
     *
     * <p>Every cell of the sunk ship has been fired at, and no two ships touch, so
     * the cells reachable from this one through hits are exactly that ship.
     *
     * @param lastCell the cell whose shot answered SUNK
     * @return how many squares the sunk ship occupied
     */
    private int markSunk(Coordinate lastCell) {
        Set<Coordinate> group = new HashSet<>();
        Deque<Coordinate> queue = new ArrayDeque<>();
        queue.add(lastCell);
        group.add(lastCell);
        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int row = current.getRow() + dr;
                    int column = current.getColumn() + dc;
                    if (row < 0 || column < 0) {
                        continue;
                    }
                    Coordinate neighbour = new Coordinate(row, column);
                    ShotResult state = seen.get(neighbour);
                    boolean isShip = state == ShotResult.HIT || state == ShotResult.SUNK;
                    if (isShip && group.add(neighbour)) {
                        queue.add(neighbour);
                    }
                }
            }
        }
        int length = group.size();
        for (int i = 0; i < SHIP_TYPES.length; i++) {
            if (!sunkFlags.get(i) && SHIP_TYPES[i].getLength() == length) {
                sunkFlags.set(i, Boolean.TRUE);
                break;
            }
        }
        return length;
    }

    private void finishIfOver() {
        if (!session.over()) {
            return;
        }
        for (CellButton[] row : cells) {
            for (CellButton cell : row) {
                cell.setEnabled(false);
            }
        }
        if (session.won()) {
            setStatus("Victory. Every ship sunk in " + session.shots + " shots.");
            JOptionPane.showMessageDialog(this,
                    "You sank the whole fleet in " + session.shots + " shots.",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
        } else {
            setStatus("Out of shots. " + remainingShips() + " ship(s) still afloat.");
            JOptionPane.showMessageDialog(this,
                    "You used all " + session.shotLimit + " shots and "
                            + remainingShips() + " ship(s) survived.",
                    "Out of shots", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int remainingShips() {
        int afloat = 0;
        for (Boolean sunk : sunkFlags) {
            if (!sunk) {
                afloat++;
            }
        }
        return afloat;
    }

    private void refresh() {
        int shipCells = 0;
        for (ShipType type : SHIP_TYPES) {
            shipCells += type.getLength();
        }
        shotsValue.setText(session.shots + " / " + session.shotLimit);
        hitsValue.setText(session.hits + " / " + shipCells);
        sunkValue.setText((SHIP_TYPES.length - remainingShips()) + " / " + SHIP_TYPES.length);

        fleetPanel.removeAll();
        for (int i = 0; i < SHIP_TYPES.length; i++) {
            if (i > 0) {
                fleetPanel.add(Box.createVerticalStrut(12));
            }
            fleetPanel.add(new FleetRow(SHIP_NAMES[i], SHIP_TYPES[i].getLength(), sunkFlags.get(i)));
        }
        fleetPanel.revalidate();
        fleetPanel.repaint();
        if (cells != null) {
            for (CellButton[] row : cells) {
                for (CellButton cell : row) {
                    cell.repaint();
                }
            }
        }
    }

    private void setStatus(String text) {
        status.setText(text);
    }

    private void showRules() {
        JOptionPane.showMessageDialog(this,
                "<html><body style='width:330px;font-family:Arial;font-size:11px'>"
                + "The grid hides one Battleship of four squares, two Cruisers of three "
                + "and three Destroyers of two.<br><br>"
                + "No two ships overlap and no two touch, not even at a corner.<br><br>"
                + "Blue is a miss, red is a hit. Sink every ship before the shots run out."
                + "</body></html>",
                "Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Opens the prototype window.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            capture(args[0]);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ignored) {
                // The default look and feel is acceptable.
            }
            new BattleshipPrototype().setVisible(true);
        });
    }

    /** Saves a picture of the window part-way through a game, for review. */
    private static void capture(String file) throws Exception {
        final BattleshipPrototype[] holder = new BattleshipPrototype[1];
        SwingUtilities.invokeAndWait(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ignored) {
                // The default look and feel is acceptable.
            }
            holder[0] = new BattleshipPrototype();
            holder[0].setVisible(true);
        });
        BattleshipPrototype window = holder[0];
        Thread.sleep(400);
        // Fire until something has been sunk, so the panel has something to show.
        for (CellButton[] row : window.cells) {
            for (CellButton cell : row) {
                final boolean[] stop = {false};
                SwingUtilities.invokeAndWait(() -> {
                    if (window.session.hits >= 4 && window.session.shots >= 18) {
                        stop[0] = true;
                    } else if (cell.isEnabled()) {
                        cell.doClick();
                    }
                });
                if (stop[0]) {
                    break;
                }
            }
        }
        Thread.sleep(300);
        SwingUtilities.invokeAndWait(() -> {
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
                    window.getWidth(), window.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            window.printAll(g);
            g.dispose();
            try {
                javax.imageio.ImageIO.write(image, "png", new java.io.File(file));
                System.out.println("wrote " + file + " " + image.getWidth() + "x" + image.getHeight());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
        System.exit(0);
    }
}
