package battleship;

import battleship.controller.GameController;
import battleship.view.GameWindow;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point: starts the Battleship game.
 *
 * <p>OOP: Abstraction. This class knows only that a controller runs a game and a
 * window shows one. It contains no rule and no drawing of its own.
 */
public final class App {

    private App() {
        // Utility entry point: no instance is needed.
    }

    /**
     * Opens the game window on Swing's event dispatch thread.
     *
     * <p>The cross-platform look and feel is requested deliberately. Some native
     * look and feels ignore a button's background colour, and the brief requires
     * cells to be shown in blue and red, so a look and feel that honours those
     * colours is a requirement rather than a preference.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedOperationException
                    | javax.swing.UnsupportedLookAndFeelException ignored) {
                // The default look and feel is acceptable; the game still runs.
            }
            new GameWindow(new GameController()).setVisible(true);
        });
    }
}
