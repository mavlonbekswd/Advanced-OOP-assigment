package battleship.view;

import battleship.model.ShipStatus;
import battleship.model.ShotResult;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * The list of enemy ships beside the grid, with the running counts underneath.
 *
 * <p>What this panel shows is exactly what a player is entitled to know: which
 * kinds of ship are out there, which of them have gone down, and how the hunt is
 * going. It is given a list of {@link ShipStatus}, which carries a type and a
 * condition and no position at all, so no amount of reading this class can reveal
 * where a ship that is still afloat is hiding.
 *
 * <p>Ships of the same kind are numbered in the order the computer placed them.
 * Two Cruisers are identical, so "Cruiser 1" names a line in this list rather than
 * a particular ship on the board.
 *
 * <p>OOP: Encapsulation. The labels are private and are only ever changed by
 * {@link #update(List, int, int, int)}.
 */
public final class FleetPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Holds one line per ship; rebuilt whenever the fleet changes. */
    private final JPanel ships = new JPanel();

    /** Shots taken, against the limit the setting allows. */
    private final JLabel shots = new JLabel();

    /** Shots that found a ship, against the squares the fleet covers. */
    private final JLabel hits = new JLabel();

    /** Ships destroyed, against the size of the fleet. */
    private final JLabel sunk = new JLabel();

    /** Builds an empty panel; it fills in once a game is under way. */
    public FleetPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Enemy fleet"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));

        ships.setLayout(new BoxLayout(ships, BoxLayout.Y_AXIS));
        ships.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(ships);

        JSeparator rule = new JSeparator();
        rule.setAlignmentX(Component.LEFT_ALIGNMENT);
        rule.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        add(rule);

        add(counter("Shots", shots));
        add(counter("Hits", hits));
        add(counter("Sunk", sunk));
    }

    /**
     * Redraws the panel from the current state of the game.
     *
     * @param fleet the condition of every ship, in the order they were placed
     * @param shotCount how many shots the player has taken
     * @param shotLimit how many shots this game allows
     * @param hitCount how many of those shots found a ship
     * @throws NullPointerException if fleet is null
     */
    public void update(List<ShipStatus> fleet, int shotCount, int shotLimit, int hitCount) {
        Objects.requireNonNull(fleet, "Fleet is required");

        ships.removeAll();
        int squares = 0;
        int lost = 0;
        for (int i = 0; i < fleet.size(); i++) {
            ShipStatus ship = fleet.get(i);
            squares += ship.getLength();
            if (ship.isSunk()) {
                lost++;
            }
            ships.add(shipLine(ship, numberWithin(fleet, i)));
        }
        ships.revalidate();
        ships.repaint();

        shots.setText(shotCount + " / " + shotLimit);
        hits.setText(hitCount + " / " + squares);
        sunk.setText(lost + " / " + fleet.size());
    }

    /**
     * Builds the line for one ship.
     *
     * @param ship the ship to describe
     * @param number its position among ships of the same kind, or 0 if it is the
     *        only one of its kind
     * @return the finished line
     */
    private static JPanel shipLine(ShipStatus ship, int number) {
        String name = title(ship.getType().name());
        if (number > 0) {
            name = name + " " + number;
        }

        JLabel left = new JLabel(name + "  (" + ship.getLength() + ")");
        JLabel right = new JLabel(ship.isSunk() ? "sunk" : "");
        if (ship.isSunk()) {
            // The wording carries the meaning; the colour only reinforces it, so
            // the panel still reads correctly in a black and white printout.
            left.setForeground(CellStyle.colourFor(ShotResult.SUNK));
            right.setForeground(left.getForeground());
            left.setFont(left.getFont().deriveFont(Font.BOLD));
        }

        JPanel line = new JPanel(new BorderLayout(8, 0));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        line.add(left, BorderLayout.WEST);
        line.add(right, BorderLayout.EAST);
        return line;
    }

    /**
     * Works out how to number a ship among others of the same kind.
     *
     * @param fleet the whole fleet
     * @param index the position of the ship being numbered
     * @return its number within its kind, or 0 when its kind appears only once
     */
    private static int numberWithin(List<ShipStatus> fleet, int index) {
        int total = 0;
        int position = 0;
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).getType() == fleet.get(index).getType()) {
                total++;
                if (i <= index) {
                    position++;
                }
            }
        }
        return total > 1 ? position : 0;
    }

    /**
     * Turns an enum name such as BATTLESHIP into something a player would read.
     *
     * @param name the constant name
     * @return the same word with only its first letter capitalised
     */
    private static String title(String name) {
        return name.charAt(0) + name.substring(1).toLowerCase(Locale.ENGLISH);
    }

    /**
     * Builds one of the counters under the list.
     *
     * @param caption what the number means
     * @param value the label that will hold the number
     * @return the finished row
     */
    private static JPanel counter(String caption, JLabel value) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.add(new JLabel(caption), BorderLayout.WEST);
        value.setFont(value.getFont().deriveFont(Font.BOLD));
        row.add(value, BorderLayout.EAST);
        return row;
    }
}
