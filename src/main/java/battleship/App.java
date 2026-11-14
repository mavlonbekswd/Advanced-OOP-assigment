package battleship;

import battleship.model.Coordinate;

/**
 * Temporary entry point for demonstrating the first domain class.
 * This will later launch the Battleship GUI.
 */
public final class App {
    private App() {
        // Utility entry point: no instance is needed.
    }

    /**
     * Prints a sample board coordinate.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Coordinate target = new Coordinate(3, 5);
        System.out.println("Target: " + target);
        System.out.println("On 10x10 board: " + target.isWithin(10));
    }
}
