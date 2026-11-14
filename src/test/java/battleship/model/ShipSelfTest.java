package battleship.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Dependency-free domain checks; this harness is not JUnit. */
public final class ShipSelfTest {
    private static int checks;

    public static void main(String[] args) {
        for (ShipType type : ShipType.values()) {
            List<Coordinate> cells = new ArrayList<>();
            for (int i = 0; i < type.getLength(); i++) cells.add(new Coordinate(0, i));
            Ship ship = new Ship(type, cells);
            check(ship.getType() == type && !ship.isSunk(), "Initial type and state");
            cells.clear();
            check(ship.getPositions().size() == type.getLength(), "Defensive copy");
            rejects(UnsupportedOperationException.class, () -> ship.getPositions().clear());
            check(!ship.registerHit(new Coordinate(9, 9)) && ship.getHitCount() == 0, "Miss unchanged");
            for (int i = 0; i < type.getLength(); i++) {
                check(ship.registerHit(new Coordinate(0, i)), "New hit accepted");
                check(!ship.registerHit(new Coordinate(0, i)), "Duplicate hit ignored");
                check(ship.isSunk() == (i == type.getLength() - 1), "Sunk only after final hit");
            }
            check(ship.getHitCount() == type.getLength(), "Distinct hit count");
        }
        Ship vertical = new Ship(ShipType.CRUISER, Arrays.asList(c(3, 2), c(1, 2), c(2, 2)));
        check(vertical.getPositions().get(0).equals(c(1, 2)), "Unordered vertical cells accepted");
        rejects(IllegalArgumentException.class, () -> new Ship(ShipType.CRUISER, Arrays.asList(c(0, 0), c(0, 1))));
        rejects(IllegalArgumentException.class, () -> new Ship(ShipType.DESTROYER, Arrays.asList(c(0, 0), c(0, 0))));
        rejects(IllegalArgumentException.class, () -> new Ship(ShipType.DESTROYER, Arrays.asList(c(0, 0), c(1, 1))));
        rejects(IllegalArgumentException.class, () -> new Ship(ShipType.CRUISER, Arrays.asList(c(0, 0), c(0, 1), c(1, 1))));
        rejects(IllegalArgumentException.class, () -> new Ship(ShipType.DESTROYER, Arrays.asList(c(0, 0), c(0, 2))));
        rejects(NullPointerException.class, () -> new Ship(null, Arrays.asList(c(0, 0), c(0, 1))));
        rejects(NullPointerException.class, () -> new Ship(ShipType.DESTROYER, null));
        rejects(NullPointerException.class, () -> new Ship(ShipType.DESTROYER, Arrays.asList(c(0, 0), null)));
        rejects(NullPointerException.class, () -> vertical.registerHit(null));
        System.out.println("PASS: " + checks + " Ship self-checks");
    }

    private static Coordinate c(int row, int column) { return new Coordinate(row, column); }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
        checks++;
    }

    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Unexpected exception", error);
            checks++;
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
