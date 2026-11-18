import battleship.controller.GameController;
import battleship.service.RandomShipPlacer;
import battleship.view.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Drives the real GameWindow and saves images of the states it reaches. */
public class Capture {
    static GameWindow window;
    static List<JButton> cells = new ArrayList<>();
    static File dir;

    public static void main(String[] a) throws Exception {
        dir = new File(a[0]);
        dir.mkdirs();
        SwingUtilities.invokeAndWait(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) { }
            window = new GameWindow(new GameController(new RandomShipPlacer(new Random(2026))));
            window.setVisible(true);
            collect(window, cells);
        });
        settle();
        save("01-new-game.png");

        // Fire until there are a few misses and at least two hits.
        clickUntil(() -> count("X") + count("#") >= 2 && count("o") >= 4);
        settle(); save("02-in-progress.png");

        // Keep firing until a ship is completely sunk.
        clickUntil(() -> count("#") >= 2);
        settle(); save("03-ship-sunk.png");

        // Play to victory. The final click opens a modal dialog, so the click and
        // the capture are queued separately; the modal loop still pumps events.
        final int[] i = {0};
        SwingUtilities.invokeLater(() -> {
            for (JButton c : cells) if (c.isEnabled()) c.doClick();
        });
        Thread.sleep(1500);
        SwingUtilities.invokeLater(() -> {
            try {
                Window dlg = null;
                for (Window w : Window.getWindows())
                    if (w instanceof JDialog && w.isVisible()) dlg = w;
                write(window, "04-victory-board.png");
                if (dlg != null) { write(dlg, "05-victory-message.png"); dlg.setVisible(false); }
            } catch (Exception e) { e.printStackTrace(); }
        });
        Thread.sleep(1500);

        SwingUtilities.invokeAndWait(() -> {
            for (Component c : allOf(window))
                if (c instanceof JButton && "New Game".equals(((JButton) c).getText()))
                    ((JButton) c).doClick();
        });
        settle(); save("06-after-restart.png");

        System.out.println("saved to " + dir);
        System.exit(0);
    }

    static void clickUntil(java.util.function.BooleanSupplier done) throws Exception {
        for (JButton c : cells) {
            if (!c.isEnabled()) continue;
            SwingUtilities.invokeAndWait(c::doClick);
            final boolean[] f = {false};
            SwingUtilities.invokeAndWait(() -> f[0] = done.getAsBoolean());
            if (f[0]) return;
        }
    }

    static int count(String glyph) {
        int n = 0;
        for (JButton c : cells) if (glyph.equals(c.getText())) n++;
        return n;
    }

    static void settle() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
        Thread.sleep(250);
    }

    static void save(String name) throws Exception {
        SwingUtilities.invokeAndWait(() -> { try { write(window, name); } catch (Exception e) { e.printStackTrace(); } });
    }

    static void write(Window w, String name) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(1, w.getWidth()), Math.max(1, w.getHeight()),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        w.printAll(g);
        g.dispose();
        ImageIO.write(img, "png", new File(dir, name));
        System.out.println("wrote " + name + " " + img.getWidth() + "x" + img.getHeight());
    }

    static void collect(Container c, List<JButton> out) {
        for (Component ch : c.getComponents()) {
            if (ch instanceof JButton && ch.getPreferredSize().width == 42) out.add((JButton) ch);
            if (ch instanceof Container) collect((Container) ch, out);
        }
    }

    static List<Component> allOf(Container c) {
        List<Component> out = new ArrayList<>();
        for (Component ch : c.getComponents()) {
            out.add(ch);
            if (ch instanceof Container) out.addAll(allOf((Container) ch));
        }
        return out;
    }
}
