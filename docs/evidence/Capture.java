import battleship.controller.GameController;
import battleship.service.RandomShipPlacer;
import battleship.view.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Drives the real GameWindow through its own buttons and saves images of the
 * states it reaches, so the figures in the report come from the application
 * rather than from a mock-up.
 *
 * <p>The fleet generator is given a seeded random source here, so the same
 * images can be produced again. The application itself uses an unseeded one.
 *
 * <p>Since the game now limits how many shots are allowed, clicking every
 * square in turn no longer wins on Normal: the seventy shots run out first.
 * The harness therefore plays the way a person does, firing on a spaced-out
 * pattern until something is hit and then working outwards from that hit.
 */
public class Capture {
    static GameWindow window;
    static List<JButton> cells = new ArrayList<>();
    static Deque<Integer> targets = new ArrayDeque<>();
    static int size;
    static File dir;

    public static void main(String[] args) throws Exception {
        dir = new File(args[0]);
        dir.mkdirs();

        SwingUtilities.invokeAndWait(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) { }
            window = new GameWindow(new GameController(new RandomShipPlacer(new Random(2026))));
            window.setVisible(true);
            collect();
        });
        settle();
        save("01-new-game.png");

        // A few shots in: enough misses to show the blue, enough hits to show the red.
        playUntil(() -> marks("X") + marks("#") >= 2 && marks("o") >= 4);
        settle();
        save("02-in-progress.png");

        // Keep playing until half the fleet has gone down, so this picture is
        // clearly further on than the last one rather than a shot or two later.
        playUntil(() -> marks("#") >= 3);
        settle();
        save("03-ship-sunk.png");

        finish("04-victory-board.png", "05-victory-message.png");

        clickButton("New Game");
        settle();
        save("06-after-restart.png");

        // A board cleared for a new game looks exactly like one that has never
        // been played, so the only way to show that the fleet really was placed
        // again is to play the second game out and reveal it.
        finish("07-second-victory-board.png", null);

        // The last three images are the evidence for the difficulty setting,
        // which none of the others can show: the grid is a different size, and
        // a game can now be lost as well as won.
        SwingUtilities.invokeAndWait(() -> setDifficulty("Easy"));
        settle();
        save("08-easy-board.png");

        SwingUtilities.invokeAndWait(() -> setDifficulty("Hard"));
        settle();
        sweep("09-hard-out-of-shots.png", "10-out-of-shots-message.png");

        System.out.println("saved to " + dir);
        System.exit(0);
    }

    /** Plays one shot at a time until the condition holds, or the game ends. */
    static void playUntil(java.util.function.BooleanSupplier done) throws Exception {
        for (int guard = 0; guard < 400; guard++) {
            final boolean[] fired = {false};
            SwingUtilities.invokeAndWait(() -> fired[0] = step());
            if (!fired[0]) return;
            final boolean[] stop = {false};
            SwingUtilities.invokeAndWait(() -> stop[0] = done.getAsBoolean());
            if (stop[0]) return;
        }
    }

    /**
     * Plays the rest of the game out and photographs the end of it.
     *
     * <p>The shot that ends a game opens a modal dialog, which blocks whichever
     * thread fired it. The play and the photograph are therefore queued as two
     * separate pieces of work: the dialog's own event loop still runs the
     * second one, which is what makes it possible to photograph the dialog.
     *
     * @param boardImage where to save the picture of the finished board
     * @param dialogImage where to save the dialog, or null to just dismiss it
     */
    static void finish(String boardImage, String dialogImage) throws Exception {
        SwingUtilities.invokeLater(() -> { while (step()) { } });
        Thread.sleep(6000);
        photograph(boardImage, dialogImage);
        Thread.sleep(6000);
    }

    /**
     * Fires at every square in order instead of hunting, which uses the shots
     * up long before an unguided sweep could find all sixteen ship squares.
     *
     * @param boardImage where to save the picture of the finished board
     * @param dialogImage where to save the dialog
     */
    static void sweep(String boardImage, String dialogImage) throws Exception {
        SwingUtilities.invokeLater(() -> {
            for (JButton cell : cells) {
                if (cell.isEnabled()) {
                    cell.doClick();
                }
            }
        });
        Thread.sleep(6000);
        photograph(boardImage, dialogImage);
        Thread.sleep(6000);
    }

    /** Saves the window, and whichever dialog is covering it, then closes the dialog. */
    static void photograph(String boardImage, String dialogImage) {
        SwingUtilities.invokeLater(() -> {
            try {
                Window dialog = null;
                for (Window open : Window.getWindows()) {
                    if (open instanceof JDialog && open.isVisible()) {
                        dialog = open;
                    }
                }
                write(window, boardImage);
                if (dialog != null) {
                    if (dialogImage != null) {
                        write(dialog, dialogImage);
                    }
                    dialog.setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Fires one shot the way a person would.
     *
     * <p>With nothing to follow up, it fires on every other square, because the
     * shortest ship covers two squares and so cannot hide between them. Once a
     * shot hits, the squares around it are tried first until that ship goes
     * down and the trail is dropped.
     *
     * @return whether a shot was actually fired
     */
    static boolean step() {
        int index = nextTarget();
        if (index < 0) {
            index = nextHuntingSquare();
        }
        if (index < 0) {
            return false;
        }
        JButton cell = cells.get(index);
        cell.doClick();
        String mark = cell.getText();
        if ("X".equals(mark)) {
            queueNeighbours(index);
        } else if ("#".equals(mark)) {
            targets.clear();
        }
        return true;
    }

    /** @return a square next to an earlier hit, or -1 if there is none left */
    static int nextTarget() {
        while (!targets.isEmpty()) {
            int index = targets.removeFirst();
            if (cells.get(index).isEnabled()) {
                return index;
            }
        }
        return -1;
    }

    /** @return the next square of the spaced-out pattern, or any square left */
    static int nextHuntingSquare() {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).isEnabled() && (i / size + i % size) % 2 == 0) {
                return i;
            }
        }
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i).isEnabled()) {
                return i;
            }
        }
        return -1;
    }

    /** Lines up the four squares around a hit to be tried next. */
    static void queueNeighbours(int index) {
        int row = index / size;
        int column = index % size;
        if (row > 0) { targets.addLast(index - size); }
        if (row < size - 1) { targets.addLast(index + size); }
        if (column > 0) { targets.addLast(index - 1); }
        if (column < size - 1) { targets.addLast(index + 1); }
    }

    /** Chooses a setting from the toolbar exactly as a player would. */
    static void setDifficulty(String label) {
        for (Component component : allOf(window)) {
            if (component instanceof JComboBox) {
                JComboBox<?> box = (JComboBox<?>) component;
                for (int i = 0; i < box.getItemCount(); i++) {
                    if (label.equals(String.valueOf(box.getItemAt(i)))) {
                        box.setSelectedIndex(i);
                    }
                }
            }
        }
        collect();
    }

    /** Presses the named button in the toolbar. */
    static void clickButton(String label) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (Component component : allOf(window)) {
                if (component instanceof JButton && label.equals(((JButton) component).getText())) {
                    ((JButton) component).doClick();
                }
            }
            collect();
        });
    }

    /** @return how many squares currently show the given mark */
    static int marks(String glyph) {
        int total = 0;
        for (JButton cell : cells) {
            if (glyph.equals(cell.getText())) {
                total++;
            }
        }
        return total;
    }

    /**
     * Finds the squares of the grid again. Changing the setting builds a new
     * grid, so the old buttons are no longer the ones on screen.
     */
    static void collect() {
        cells.clear();
        targets.clear();
        gather(window, cells);
        size = (int) Math.round(Math.sqrt(cells.size()));
    }

    /** A grid square is a button built at the grid's own fixed size. */
    static void gather(Container parent, List<JButton> out) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && child.getPreferredSize().width == 42) {
                out.add((JButton) child);
            }
            if (child instanceof Container) {
                gather((Container) child, out);
            }
        }
    }

    static List<Component> allOf(Container parent) {
        List<Component> out = new ArrayList<>();
        for (Component child : parent.getComponents()) {
            out.add(child);
            if (child instanceof Container) {
                out.addAll(allOf((Container) child));
            }
        }
        return out;
    }

    static void settle() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
        Thread.sleep(250);
    }

    static void save(String name) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try { write(window, name); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    static void write(Window source, String name) throws Exception {
        BufferedImage image = new BufferedImage(Math.max(1, source.getWidth()),
                Math.max(1, source.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        source.printAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", new File(dir, name));
        System.out.println("wrote " + name + " " + image.getWidth() + "x" + image.getHeight());
    }
}
